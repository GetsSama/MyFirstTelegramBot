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
    public void cashed(Session session) {
        this.cash.put(session.getIdentifier(), session);
        log.info("Add in cash: " + session.getIdentifier());
    }

    @Override
    public Session getSession(String identifier) {
        return this.cash.get(identifier);
    }

    @Override
    public void removeSession(Session session) {
        this.cash.remove(session.getIdentifier());
        log.info("Remove from cash: " + session.getIdentifier());
    }
}
