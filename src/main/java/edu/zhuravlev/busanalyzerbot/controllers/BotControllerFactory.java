package edu.zhuravlev.busanalyzerbot.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Primary
@Slf4j
public class BotControllerFactory implements BotController{
    private ApplicationContext context;
    @Autowired
    private void setContext(ApplicationContext context) {
        this.context = context;
    }
    @Override
    public void processUpdate(Update update) {
        if(update.hasMessage()&&update.getMessage().hasText())
            if(update.getMessage().getText().equals("/start"))
              context.getBean(StartController.class).processUpdate(update);
        log.info(update.toString());
    }
}
