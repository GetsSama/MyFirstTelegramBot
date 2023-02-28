package edu.zhuravlev.busanalyzerbot.cashed.sessions;

import edu.zhuravlev.busanalyzerbot.cashed.cash.SessionCash;
import edu.zhuravlev.busanalyzerbot.controllers.AddBusStopController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class SessionFactory {
    private SessionCash cash;

    @Autowired
    private ApplicationContext context;

    @Autowired
    public void setCash(SessionCash cash) {
        this.cash = cash;
    }

    //@Lookup
    private AddBusStopController getAddBotController() {
        //return null;
        return context.getBean(AddBusStopController.class);
    }

    public void newSessionAddBusStop(Update update) {
        var addBusController = getAddBotController();
        var chatId = update.getMessage().getChatId().toString();

        var sessionMonitor = new Thread(addBusController);
        sessionMonitor.start();

        new DefaultSession(cash, chatId, chatId, addBusController, sessionMonitor);
        addBusController.processUpdate(update);
    }
}
