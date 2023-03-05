package edu.zhuravlev.busanalyzerbot.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BotControllerFactory {
    private ApplicationContext context;
    @Autowired
    public void setContext(ApplicationContext context) {
        this.context = context;
    }

    public BotController getBotController(String command) {
        return (BotController)context.getBean(command);
    }
}
