package edu.zhuravlev.busanalyzerbot.cashed.sessions;

import edu.zhuravlev.busanalyzerbot.cashed.cash.SessionCash;
import edu.zhuravlev.busanalyzerbot.controllers.BotController;
import edu.zhuravlev.busanalyzerbot.controllers.MainStateController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@Scope("prototype")
public class DefaultSession extends AbstractSession{
    private Session mainSession;

    @Override
    public void setPrimaryIdentifier(String primaryIdentifier) {
        super.setPrimaryIdentifier(primaryIdentifier);
        this.mainSession = cash.getSession(getPrimaryIdentifier());
    }

    @Override
    public void run() {
        cash.removeSession(mainSession);
        var mainController = (MainStateController)mainSession.getController();
        mainController.pause();
        cash.cashed(this);
        try {
                synchronized (joiningControllerThread) {
                    joiningControllerThread.join();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        cash.removeSession(this);
        cash.cashed(mainSession);
        mainController.resume();
    }
}
