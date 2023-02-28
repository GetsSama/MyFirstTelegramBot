package edu.zhuravlev.busanalyzerbot.controllers;

import busentity.Bus;
import busparser.BusParser;
import busparser.DefaultBusParser;
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

import java.util.Set;

@Component
@Scope("prototype")
public class AddBusStopController implements BotController, Runnable{
    private ControllerState state;
    private String chatId;
    private Update update;
    private AbsSender sender;
    private BusParser parser;
    private boolean onProcess = true;
    private String busStopName;
    private String busStopUrl;
    private Set<String> priorityBuses;

    public AddBusStopController() {
        this.state = ControllerState.NEW;
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
                        synchronized (this) {
                            wait();
                        }
                    }
                    case CHOOSE_BUSES -> {

                    }
                    case FINAL -> {

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
        var message = new SendMessage();
        message.setText("Введите имя для новой остановки");
        message.setChatId(chatId);
        send(message);
    }

    private void parseUrlState() {
        var message = new SendMessage();
        message.setText("Введите URL новой остановки");
        message.setChatId(chatId);
    }

    private void chooseBusesState() {
        var allBuses = parser.parse(busStopUrl);
        var allBusesName = allBuses.stream().map(Bus::getBusName).toList();
        var poll = new SendPoll();

        poll.setChatId(chatId);
        poll.setQuestion("Какие автобусы отслеживать?");
        poll.setOptions(allBusesName);
        poll.setAllowMultipleAnswers(true);

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
}
