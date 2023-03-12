package edu.zhuravlev.busanalyzerbot.controllers;

import edu.zhuravlev.busanalyzerbot.botcommands.MyCommands;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.List;

@Component("/help")
public class HelpController extends AbstractBotController{
    private final String preparedMessage;

    @Autowired
    public HelpController(List<BotCommand> commands) {
        super();
        var builder = new StringBuilder();
        builder.append("There is fully list of this bot commands:\n");
        for (var command : commands) {
            builder.append(command.getCommand());
            builder.append(" - ");
            builder.append(command.getDescription());
            builder.append("\n");
        }

        this.preparedMessage = builder.toString();
    }
    @Override
    public void processUpdate(Update update) {
        setChatId(update.getMessage().getChatId().toString());
        sendSimpleMessage(preparedMessage);
    }
}