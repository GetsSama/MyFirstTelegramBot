package edu.zhuravlev.busanalyzerbot.controllers;

import edu.zhuravlev.busanalyzerbot.entities.User;
import edu.zhuravlev.busanalyzerbot.services.userservice.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Set;

@Component
@NoArgsConstructor
@AllArgsConstructor
public class StartController implements BotController {
    private AbsSender bot;
    private final String helloMessage = "Привет! Это телеграм-бот отслеживающий расписание автобусов на остановках.\nВот список моих команд:\n";
    private final String addBusStop = "/addBusStop - добавить остановку для отслеживания";
    private UserService userService;
    @Autowired
    public void setBot(AbsSender bot) {
        this.bot = bot;
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

        if(!userService.hasUser(chatId))
            userService.addUser(newUser);

        var message = new SendMessage();
        message.setChatId(chatId);
        message.setText(helloMessage + addBusStop);

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
