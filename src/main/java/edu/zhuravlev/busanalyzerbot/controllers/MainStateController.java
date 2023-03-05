package edu.zhuravlev.busanalyzerbot.controllers;

import edu.zhuravlev.busanalyzerbot.cashed.sessions.Sessional;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component("/main")
@Scope("prototype")
public class MainStateController implements BotController, Sessional {
    @Override
    public void processUpdate(Update update) {

    }
}
