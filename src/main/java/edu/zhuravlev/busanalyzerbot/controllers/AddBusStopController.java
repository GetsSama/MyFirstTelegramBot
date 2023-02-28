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
    private Update lastUpdate;
    private AbsSender sender;
    private boolean onProcess = true;

    public AddBusStopController() {
        this.state = ControllerState.NEW;
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
        try {
            while (onProcess) {
                if (lastUpdate == null)
                    synchronized (this) {
                        wait();
                    }
                switch (state) {
                    case NEW -> {
                        sendState("First state!");
                        state = ControllerState.MEDIUM;
                        synchronized (this) {
                            wait();
                        }
                    }
                    case MEDIUM -> {
                        sendState("Second state!");
                        state = ControllerState.FINAL;
                        synchronized (this) {
                            wait();
                        }
                    }
                    case FINAL -> {
                        sendState("Third state!");
                        onProcess = false;
                        //Thread.currentThread().notify();
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
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
