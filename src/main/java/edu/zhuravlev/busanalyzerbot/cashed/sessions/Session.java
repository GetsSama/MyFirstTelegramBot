package edu.zhuravlev.busanalyzerbot.cashed.sessions;

import edu.zhuravlev.busanalyzerbot.controllers.BotController;

import java.util.Set;

public interface Session {
    String getIdentifier();
    Set<String> getIdentifiers();
    BotController getController();
}
