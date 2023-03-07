package edu.zhuravlev.busanalyzerbot.cashed.sessions;

import edu.zhuravlev.busanalyzerbot.controllers.BotController;

import java.util.Set;

public interface Session extends Runnable{
    String getPrimaryIdentifier();
    Set<String> getIdentifiers();
    void addForeignIdentifier(String identifier);
    BotController getController();
}
