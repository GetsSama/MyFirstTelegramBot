package edu.zhuravlev.busanalyzerbot.controllers;

import edu.zhuravlev.busanalyzerbot.BotConfig;
import edu.zhuravlev.busanalyzerbot.MainStateInitializer;
import edu.zhuravlev.busanalyzerbot.entities.User;
import edu.zhuravlev.busanalyzerbot.services.userservice.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Set;

@Component("/start")
@NoArgsConstructor
@AllArgsConstructor
public class StartController extends AbstractBotController{
    private BotConfig botConfig;
    private UserService userService;
    private MainStateInitializer initializer;
    private BotController helpController;

    @Autowired
    @Qualifier("/help")
    public void setHelpController(BotController helpController) {
        this.helpController = helpController;
    }
    @Autowired
    public void setInitializer(MainStateInitializer initializer) {
        this.initializer = initializer;
    }
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    @Autowired
    public void setBotConfig(BotConfig botConfig) {
        this.botConfig = botConfig;
    }


    @Override
    public void processUpdate(Update update) {
        setChatId(update.getMessage().getChatId().toString());
        var userName = update.getMessage().getFrom().getFirstName();

        var newUser = new User(userName, chatId, Set.of());

        if(!userService.hasUser(chatId)) {
            userService.addUser(newUser);
            initializer.initNew(newUser);
        }

        sendSimpleMessage(botConfig.getHelloMessage());
        helpController.processUpdate(update);
    }
}
