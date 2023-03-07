package edu.zhuravlev.busanalyzerbot.cashed.sessions;

import edu.zhuravlev.busanalyzerbot.cashed.cash.SessionCash;
import edu.zhuravlev.busanalyzerbot.controllers.BotController;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractSession implements Session{
    private String primaryIdentifier;
    private final Set<String> foreignIdentifiers = new HashSet<>();
    private BotController controller;
    protected SessionCash cash;
    protected Thread joiningControllerThread;
    @Override
    public String getPrimaryIdentifier() {
        return this.primaryIdentifier;
    }

    @Override
    public Set<String> getIdentifiers() {
        this.foreignIdentifiers.add(this.getPrimaryIdentifier());
        return this.foreignIdentifiers;
    }

    @Override
    public void addForeignIdentifier(String identifier) {
        this.foreignIdentifiers.add(identifier);
    }

    @Override
    public BotController getController() {
        return this.controller;
    }

    @Autowired
    public void setCash(SessionCash cash) {
        this.cash = cash;
    }

    public void setPrimaryIdentifier(String primaryIdentifier) {
        this.primaryIdentifier = primaryIdentifier;
    }

    public void setController(BotController controller) {
        this.controller = controller;
    }

    public void setJoiningControllerThread(Thread joiningControllerThread) {
        this.joiningControllerThread = joiningControllerThread;
    }
}
