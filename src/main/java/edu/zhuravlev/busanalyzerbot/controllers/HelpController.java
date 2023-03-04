package edu.zhuravlev.busanalyzerbot.controllers;

import edu.zhuravlev.busanalyzerbot.botcommands.MyCommands;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;

@Component("/help")
public class HelpController implements BotController{
    private final String preparedMessage;
    private final AbsSender sender;

    @Autowired
    public HelpController(AbsSender sender) {
        var commands = Arrays.asList(MyCommands.values());
        commands.remove(MyCommands.START);
        var builder = new StringBuilder();

        builder.append("There is fully list of this bot commands:\n");
        for (var command : commands) {
            builder.append(command.getBotCommand().getCommand());
            builder.append(" - ");
            builder.append(command.getBotCommand().getDescription());
            builder.append(";\n");
        }

        this.preparedMessage = builder.toString();
        this.sender = sender;
    }
    @Override
    public void processUpdate(Update update) {
        var helpMessage = new SendMessage();
        helpMessage.setChatId(update.getMessage().getChatId().toString());
        helpMessage.setText(preparedMessage);

        try {
            sender.execute(helpMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}