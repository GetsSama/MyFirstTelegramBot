package edu.zhuravlev.busanalyzerbot.cashed.sessions;

import edu.zhuravlev.busanalyzerbot.cashed.cash.SessionCash;
import edu.zhuravlev.busanalyzerbot.controllers.BotController;

import java.util.Set;

public class DefaultSession implements Session, Runnable{
    private SessionCash cash;
    private String primaryIdentifier;
    private BotController controller;
    private Thread joiningControllerThread;

    private Set<String> foreignIdentifiers;

    public DefaultSession() {}

    public void setPrimaryIdentifier(String primaryIdentifier) {
        this.primaryIdentifier = primaryIdentifier;
    }

    public void setController(BotController controller) {
        this.controller = controller;
    }

    public void setJoiningControllerThread(Thread joiningControllerThread) {
        this.joiningControllerThread = joiningControllerThread;
    }

    @Override
    public String getPrimaryIdentifier() {
        return this.primaryIdentifier;
    }

    @Override
    public Set<String> getIdentifiers() {
        this.foreignIdentifiers.add(primaryIdentifier);
        return foreignIdentifiers;
    }
    @Override
    public void addForeignIdentifier(String identifier) {
        this.foreignIdentifiers.add(identifier);
    }
    @Override
    public BotController getController() {
        return this.controller;
    }

    @Override
    public void run() {
        cash.cashed(this);
        try {
                synchronized (joiningControllerThread) {
                    joiningControllerThread.join();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cash.removeSession(this);
    }
}
