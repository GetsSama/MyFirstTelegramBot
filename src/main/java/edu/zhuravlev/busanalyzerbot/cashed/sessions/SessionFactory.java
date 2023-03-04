package edu.zhuravlev.busanalyzerbot.cashed.sessions;

import edu.zhuravlev.busanalyzerbot.cashed.cash.SessionCash;
import edu.zhuravlev.busanalyzerbot.controllers.AddBusStopController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

@Component
public class SessionFactory {
    private SessionCash cash;
    private ApplicationContext context;
    @Autowired
    public void setContext(ApplicationContext context) {
        this.context = context;
    }
    @Autowired
    public void setCash(SessionCash cash) {
        this.cash = cash;
    }
    public Session createSession(Class<? extends Session> sessionClass) {
        return context.getBean(sessionClass);
    }
    public Session getSessionById(String identifier) {
        return cash.getSession(identifier);
    }
}
