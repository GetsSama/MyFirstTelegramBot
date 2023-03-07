package edu.zhuravlev.busanalyzerbot.controllers;

import edu.zhuravlev.busanalyzerbot.MainStateInitializer;
import edu.zhuravlev.busanalyzerbot.botcommands.MyCommands;
import edu.zhuravlev.busanalyzerbot.entities.User;
import edu.zhuravlev.busanalyzerbot.services.userservice.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.Set;

@Component("/start")
@NoArgsConstructor
@AllArgsConstructor
public class StartController implements BotController {
    private AbsSender sender;
    private final String helloMessage = "Привет! Это телеграм-бот отслеживающий расписание автобусов на остановках.\nВот список моих команд:\n";
    private UserService userService;
    private MainStateInitializer initializer;

    @Autowired
    public void setInitializer(MainStateInitializer initializer) {
        this.initializer = initializer;
    }
    @Autowired
    public void setSender(AbsSender sender) {
        this.sender = sender;
    }
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void processUpdate(Update update) {
        var chatId = update.getMessage().getChatId().toString();
        var userName = update.getMessage().getFrom().getFirstName();

        var newUser = new User(userName, chatId, Set.of());

        if(!userService.hasUser(chatId)) {
            userService.addUser(newUser);
            initializer.initNew(newUser);
        }

        var message = new SendMessage();
        message.setChatId(chatId);
        message.setText(getAllCommands());

        try {
            sender.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private String getAllCommands() {
        var botCommands = Arrays.stream(MyCommands.values()).map(MyCommands::getBotCommand).toList();
        var builder = new StringBuilder(helloMessage);

        for(var command : botCommands) {
            builder.append(command.getCommand());
            builder.append(" - ");
            builder.append(command.getDescription());
            builder.append("\n");
        }

        return builder.toString();
    }
}
