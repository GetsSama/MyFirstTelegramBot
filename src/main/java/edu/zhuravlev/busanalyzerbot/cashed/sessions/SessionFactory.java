package edu.zhuravlev.busanalyzerbot.cashed.sessions;

import edu.zhuravlev.busanalyzerbot.cashed.cash.SessionCash;
import edu.zhuravlev.busanalyzerbot.controllers.AddBusStopController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SessionFactory {
    private SessionCash cash;
    @Autowired
    public void setCash(SessionCash cash) {
        this.cash = cash;
    }
    public Session createSession(Class<Session> sessionClass) {
        return null;
    }
    public Session getSessionById(String identifier) {
        return null;
    }
}
