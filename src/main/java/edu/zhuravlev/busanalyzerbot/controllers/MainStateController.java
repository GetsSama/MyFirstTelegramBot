package edu.zhuravlev.busanalyzerbot.controllers;

import edu.zhuravlev.busanalyzerbot.cashed.sessions.Sessional;
import edu.zhuravlev.busanalyzerbot.entities.User;
import edu.zhuravlev.busanalyzerbot.services.userservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component("main")
@Scope("prototype")
public class MainStateController implements BotController, Sessional {
    private AbsSender sender;
    private Update lastUpdate;
    private String chatId;
    private boolean isPaused = false;
    private boolean isModified = true;
    private UserService userService;
    private User user;

    public void setChatId(String chatId) {
        this.chatId = chatId;
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
    public synchronized void processUpdate(Update update) {
        this.lastUpdate = update;
        notify();
    }

    public synchronized void pause() {
        isPaused = true;
        notify();
    }

    public synchronized void resume() {
        isPaused = false;
        isModified = true;
        notify();
    }

    @Override
    public void run() {
        try {
            while(true) {
                if(isPaused) {
                    hideButtons();
                    wait();
                }
                if(isModified) {
                    refreshUser();
                    showButtons();
                }
                wait();
                processButton(lastUpdate);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void refreshUser() {
        this.user = userService.getUserByChatId(chatId);
    }

    private void showButtons() {

    }

    private void hideButtons() {

    }

    private void processButton(Update update) {

    }
}
