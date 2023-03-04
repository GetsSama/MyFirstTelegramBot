package edu.zhuravlev.busanalyzerbot.cashed.cash;

import edu.zhuravlev.busanalyzerbot.cashed.sessions.Session;

public interface SessionCash {
    void cashed(Session session);
    Session getSession(String identifier);
    void removeSession(Session session);
}
