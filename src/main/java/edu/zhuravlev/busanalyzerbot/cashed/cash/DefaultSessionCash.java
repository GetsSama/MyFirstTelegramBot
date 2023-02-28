package edu.zhuravlev.busanalyzerbot.cashed.cash;


import edu.zhuravlev.busanalyzerbot.cashed.sessions.Session;
import edu.zhuravlev.busanalyzerbot.controllers.BotController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class DefaultSessionCash implements SessionCash {
    private Map<String, Session> cash;

    public DefaultSessionCash() {
        this.cash = new HashMap<>();
    }

    @Override
    public void cashed(String identifier, Session session) {
        this.cash.put(identifier, session);
        log.info("Add in cash: " + identifier);
    }

    @Override
    public Session getSessionBean(String identifier) {
        return this.cash.get(identifier);
    }

    @Override
    public void removeSession(String identifier) {
        this.cash.remove(identifier);
        log.info("Remove from cash: " + identifier);
    }
}
