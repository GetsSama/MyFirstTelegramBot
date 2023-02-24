package edu.zhuravlev.busanalyzerbot.controllers;

import jakarta.inject.Inject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class BotControllerFactory implements BotController{
    private String qualiName;
    @Override
    public void processUpdate(Update update) {
        if(update.hasMessage()&&update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            this.qualiName = "default";

            switch (message) {
                case "/start" -> qualiName = "StartController";
            }

        }
    }
}
