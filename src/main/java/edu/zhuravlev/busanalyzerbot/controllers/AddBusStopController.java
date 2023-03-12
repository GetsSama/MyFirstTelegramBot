package edu.zhuravlev.busanalyzerbot.controllers;

import busentity.Bus;
import busparser.BusParser;
import edu.zhuravlev.busanalyzerbot.BotConfig;
import edu.zhuravlev.busanalyzerbot.cashed.sessions.SessionService;
import edu.zhuravlev.busanalyzerbot.cashed.sessions.Sessional;
import edu.zhuravlev.busanalyzerbot.controllers.service.BotControllerService;
import edu.zhuravlev.busanalyzerbot.entities.BusStop;
import edu.zhuravlev.busanalyzerbot.services.userservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@Component("/add_bus_stop")
@Scope("prototype")
public class AddBusStopController extends AbstractSessionalBotController{
    private ControllerState state;
    private BusParser parser;
    private UserService userService;
    private BotConfig botConfig;
    private BotControllerService<Set<String>> answerPollService;
    private boolean onProcess = true;
    private String busStopName;
    private String busStopUrl;
    private Set<String> priorityBuses;

    public AddBusStopController() {
        super();
        this.state = ControllerState.NEW;
    }
    @Autowired
    @Qualifier("answerPollService")
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
    public void setParser(BusParser parser) {
        this.parser = parser;
    }

    @Override
    public void run() {
        while (onProcess) {
            if (chatId == null)
                waitUpdate();
            switch (state) {
                case NEW -> {
                    chooseNameState();
                    state = ControllerState.CHOOSE_NAME;
                    waitUpdate();
                }
                case CHOOSE_NAME -> {
                    this.busStopName = lastUpdate.getMessage().getText();
                    parseUrlState();
                    state = ControllerState.PARSE_URL;
                    waitUpdate();
                }
                case PARSE_URL -> {
                    this.busStopUrl = lastUpdate.getMessage().getText();
                    chooseBusesState();
                    state = ControllerState.CHOOSE_BUSES;
                }
                case CHOOSE_BUSES -> {
                    saveAddBusStop();
                    onProcess = false;
                }
            }
        }
    }

    private void chooseNameState() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
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
        var identifierPoll = returnMessage.getPoll().getId();

        sessionService.redirectSession(chatId, identifierPoll);
        waitUpdate();
        this.priorityBuses = answerPollService.getProcessUpdateResult(lastUpdate);
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

    private enum ControllerState {
        NEW, CHOOSE_NAME, PARSE_URL, CHOOSE_BUSES
    }
}
