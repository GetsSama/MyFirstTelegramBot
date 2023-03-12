package edu.zhuravlev.busanalyzerbot.controllers;

import edu.zhuravlev.busanalyzerbot.cashed.sessions.SessionService;
import edu.zhuravlev.busanalyzerbot.cashed.sessions.Sessional;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Update;

public abstract class AbstractSessionalBotController extends AbstractBotController
                                                     implements Sessional {
    protected Update lastUpdate;
    protected SessionService sessionService;

    @Autowired
    private void setSessionService(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public synchronized void processUpdate(Update update) {
        if(chatId==null)
            setChatId(update.getMessage().getChatId().toString());
        this.lastUpdate = update;
        notify();
    }
    protected synchronized void waitUpdate() {
        try {
            wait();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
