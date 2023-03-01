package edu.zhuravlev.busanalyzerbot.controllers;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Scope("prototype")
public class MainStateController implements BotController{
    @Override
    public void processUpdate(Update update) {

    }
}
