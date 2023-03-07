package edu.zhuravlev.busanalyzerbot.cashed.sessions;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class MainSession extends AbstractSession{
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
