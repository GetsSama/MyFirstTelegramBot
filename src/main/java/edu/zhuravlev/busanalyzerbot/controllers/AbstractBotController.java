package edu.zhuravlev.busanalyzerbot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Objects;

public abstract class AbstractBotController implements BotController{
    protected AbsSender sender;
    protected String chatId;

    @Autowired
    private void setSender(AbsSender sender) {
        this.sender = sender;
    }

    protected void setChatId(String chatId) {
        this.chatId = chatId;
    }

    protected Message send(BotApiMethodMessage method) {
        try {
            return sender.execute(method);
        } catch (TelegramApiException e){
            throw new RuntimeException(e);
        }
    }

    protected void sendSimpleMessage(String textMessage) {
        Objects.requireNonNull(chatId);
        var message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textMessage);
        send(message);
    }
}
