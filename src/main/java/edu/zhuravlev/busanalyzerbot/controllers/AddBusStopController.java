package edu.zhuravlev.busanalyzerbot.controllers;

import busentity.Bus;
import busparser.BusParser;
import busparser.DefaultBusParser;
import edu.zhuravlev.busanalyzerbot.BotConfig;
import edu.zhuravlev.busanalyzerbot.cashed.sessions.SessionFactory;
import edu.zhuravlev.busanalyzerbot.controllers.service.BotControllerService;
import edu.zhuravlev.busanalyzerbot.entities.BusStop;
import edu.zhuravlev.busanalyzerbot.services.userservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@Component
@Scope("prototype")
public class AddBusStopController implements BotController, Runnable{
    private ControllerState state;
    private String chatId;
    private Update update;
    private AbsSender sender;
    private BusParser parser;
    private UserService userService;
    private BotConfig botConfig;

    private BotControllerService<Set<String>> answerPollService;
    private SessionFactory sessionFactory;
    private boolean onProcess = true;
    private String busStopName;
    private String busStopUrl;
    private Set<String> priorityBuses;

    public AddBusStopController() {
        this.state = ControllerState.NEW;
    }

    public void setPriorityBuses(Set<String> priorityBuses) {
        this.priorityBuses = priorityBuses;
        notify();
    }

    @Autowired
    public void setAnswerPollService(BotControllerService<Set<String>> answerPollService) {
        this.answerPollService = answerPollService;
    }

    @Autowired
    public void setBotConfig(BotConfig botConfig) {
        this.botConfig = botConfig;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    @Autowired
    public void setParser(BusParser parser) {
        this.parser = parser;
    }

    @Autowired
    private void setSender (AbsSender sender) {
        this.sender = sender;
    }

    @Override
    public synchronized void processUpdate(Update update) {
        if(chatId == null) {
            this.chatId = update.getMessage().getChatId().toString();
        }
        this.update = update;
        notify();
    }

    @Override
    public void run() {
        try {
            while (onProcess) {
                if (chatId == null)
                    synchronized (this) {
                        wait();
                    }
                switch (state) {
                    case NEW -> {
                        chooseNameState();
                        state = ControllerState.CHOOSE_NAME;
                        synchronized (this) {
                            wait();
                        }
                    }
                    case CHOOSE_NAME -> {
                        this.busStopName = update.getMessage().getText();
                        parseUrlState();
                        state = ControllerState.PARSE_URL;
                        synchronized (this) {
                            wait();
                        }
                    }
                    case PARSE_URL -> {
                        this.busStopUrl = update.getMessage().getText();
                        chooseBusesState();
                        state = ControllerState.CHOOSE_BUSES;
                    }
                    case CHOOSE_BUSES -> {
                        saveAddBusStop();
                        state = ControllerState.FINAL;
                    }
                    case FINAL -> {
                        goToMainAppState();
                        onProcess = false;
                        //Thread.currentThread().notify();
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void chooseNameState() {
        sendSimpleMessage("Введите имя для новой остановки");
    }

    private void parseUrlState() {
        sendSimpleMessage("Введите URL новой остановки");
    }

    private void chooseBusesState() {
        List<Bus> allBuses;
        if(botConfig.isDebugMode())
            allBuses = parser.parse(new File(botConfig.getPath()));
        else
            allBuses = parser.parse(busStopUrl);

        var allBusesName = allBuses.stream().map(Bus::getBusName).toList();
        var poll = new SendPoll();

        poll.setChatId(chatId);
        poll.setQuestion("Какие автобусы отслеживать?");
        poll.setOptions(allBusesName);
        poll.setAllowMultipleAnswers(true);

        var returnMessage = send(poll);
        var result = sessionFactory.newSessionAnswersPoll(returnMessage);
        try {
            this.priorityBuses = result.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveAddBusStop() {
        var addedBusStop = new BusStop();
        addedBusStop.setBusStopName(busStopName);
        addedBusStop.setBusStopUrl(busStopUrl);
        addedBusStop.setPriorityBuses(priorityBuses);

        var user = userService.getUserByChatId(chatId);
        user.addBusStop(addedBusStop);
        userService.updateUser(user);

        sendSimpleMessage("Новая остановка добавлена!");
    }

    private void goToMainAppState() {
        var user = userService.getUserByChatId(chatId);
        sendSimpleMessage(user.toString());
    }

    private enum ControllerState {
        NEW, CHOOSE_NAME, PARSE_URL, CHOOSE_BUSES, FINAL
    }

    private Message send(BotApiMethodMessage method) {
        try {
            return sender.execute(method);
        } catch (TelegramApiException e){
            throw new RuntimeException(e);
        }
    }

    private void sendSimpleMessage(String textMessage) {
        var message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textMessage);
        send(message);
    }
}
