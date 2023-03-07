package edu.zhuravlev.busanalyzerbot.cashed.cash;


import edu.zhuravlev.busanalyzerbot.cashed.sessions.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class DefaultSessionCash implements SessionCash {
    private Map<String, Session> cash;

    public DefaultSessionCash() {
        this.cash = new ConcurrentHashMap<>();
    }

    @Override
    public void cashed(Session session) {
        this.cash.put(session.getPrimaryIdentifier(), session);
        log.info("Add in cash: " + session.getPrimaryIdentifier());
    }
    @Override
    public void cashed(String foreignId, Session session) {
        this.cash.put(foreignId, session);
        log.info("Add in cash: " + session.getPrimaryIdentifier() + " with foreign id - " + foreignId);
    }
    @Override
    public Session getSession(String identifier) {
        return this.cash.get(identifier);
    }

    @Override
    public void removeSession(Session session) {
        var identifiers = session.getIdentifiers();
        for (var id : identifiers)
            cash.remove(id);
        log.info("Session " + session.getPrimaryIdentifier() + " remove from cash.");
    }
}
