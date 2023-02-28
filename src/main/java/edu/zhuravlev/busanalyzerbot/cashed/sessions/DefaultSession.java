package edu.zhuravlev.busanalyzerbot.cashed.sessions;

import edu.zhuravlev.busanalyzerbot.cashed.cash.SessionCash;
import edu.zhuravlev.busanalyzerbot.controllers.BotController;
import lombok.Setter;

@Setter
public class DefaultSession implements Session, Runnable{
    private SessionCash cash;
    private String identifier;
    private String chatId;
    private BotController controller;
    private Object monitor;

    public DefaultSession(SessionCash cash, String identifier, String chatId, BotController controller ,Object monitor) {
        this.cash = cash;
        this.identifier = identifier;
        this.chatId = chatId;
        this.controller = controller;
        this.monitor = monitor;

        this.cash.cashed(this.identifier, this);
        new Thread(this).start();
    }

    @Override
    public String getIdentifier() {
        return this.identifier;
    }

    @Override
    public String getChatId() {
        return this.chatId;
    }

    @Override
    public BotController getController() {
        return this.controller;
    }

    @Override
    public void run() {
        try {
            synchronized (monitor) {
                Thread t = (Thread) monitor;
                t.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        cash.removeSession(this.identifier);
    }
}
