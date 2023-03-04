package edu.zhuravlev.busanalyzerbot.cashed.sessions;

import edu.zhuravlev.busanalyzerbot.controllers.BotController;

import java.util.Set;

public interface Session {
    String getPrimaryIdentifier();
    Set<String> getIdentifiers();
    void addForeignIdentifier(String identifier);
    BotController getController();
}
