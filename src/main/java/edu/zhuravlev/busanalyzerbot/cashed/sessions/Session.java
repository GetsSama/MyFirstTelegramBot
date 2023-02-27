package edu.zhuravlev.busanalyzerbot.cashed.sessions;

import edu.zhuravlev.busanalyzerbot.controllers.BotController;

public interface Session<T extends BotController> {
    String getIdentifier();
    String getChatId();
    BotController getController();
}
