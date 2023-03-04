package edu.zhuravlev.busanalyzerbot.router;

import edu.zhuravlev.busanalyzerbot.cashed.sessions.SessionFactory;
import edu.zhuravlev.busanalyzerbot.controllers.BotControllerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class DefaultUpdateRouter implements UpdateRouter{
    private BotControllerFactory controllerFactory;
    private SessionFactory sessionFactory;

    @Autowired
    public void setControllerFactory(BotControllerFactory controllerFactory) {
        this.controllerFactory = controllerFactory;
    }

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public void process(Update update) {

    }

    @Override
    public void redirect(String from, String to) {

    }

    private String getIdentifierFromUpdate(Update update) {
        if(update.hasMessage())
            return update.getMessage().getChatId().toString();
        if(update.hasPoll())
            return update.getPoll().getId();
        else
            throw new UnsupportedOperationException("This version supports only Updates with Message or Poll.");
    }
}
