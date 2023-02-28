package edu.zhuravlev.busanalyzerbot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Scope("prototype")
public class AddBusStopController implements BotController, Runnable{
    private ControllerState state;
    private final Thread sessionThread;
    private Update lastUpdate;
    private AbsSender sender;

    public AddBusStopController() {
        this.state = ControllerState.NEW;
        this.sessionThread = new Thread(this);
        sessionThread.start();

        synchronized (this) {
            try {
                sessionThread.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Autowired
    private void setSender (AbsSender sender) {
        this.sender = sender;
    }

    @Override
    public synchronized void processUpdate(Update update) {
        this.lastUpdate = update;
        notify();
    }

    @Override
    public void run() {
        switch (state) {
            case NEW -> {sendState("First state!");
                          state = ControllerState.MEDIUM;}

            case MEDIUM -> {sendState("Second state!");
                            state = ControllerState.FINAL;}
            case FINAL -> sendState("Third state!");
        }
    }

    private void sendState(String state) {
        var message = new SendMessage();
        message.setChatId(lastUpdate.getMessage().getChatId().toString());
        message.setText(state);

        try{
            sender.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private enum ControllerState {
        NEW, MEDIUM, FINAL
    }
}
