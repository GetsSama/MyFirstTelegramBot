package edu.zhuravlev.busanalyzerbot.cashed.cash;

import edu.zhuravlev.busanalyzerbot.cashed.sessions.Session;
import edu.zhuravlev.busanalyzerbot.controllers.BotController;

public interface SessionCash {
    void cashed(String identifier, Session session);
    Session getSessionBean(String identifier);
    void removeSession(String identifier);
}
