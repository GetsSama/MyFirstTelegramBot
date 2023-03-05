package edu.zhuravlev.busanalyzerbot.cashed.sessions;

import edu.zhuravlev.busanalyzerbot.cashed.cash.SessionCash;
import edu.zhuravlev.busanalyzerbot.controllers.BotController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@Scope("prototype")
public class DefaultSession implements Session, Runnable{
    private SessionCash cash;
    private String primaryIdentifier;
    private BotController controller;
    private Thread joiningControllerThread;
    private final Set<String> foreignIdentifiers = new HashSet<>();

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

    @Autowired
    public void setCash(SessionCash cash) {
        this.cash = cash;
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
