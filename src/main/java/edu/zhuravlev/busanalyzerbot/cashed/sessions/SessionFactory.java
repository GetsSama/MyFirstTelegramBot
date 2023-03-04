package edu.zhuravlev.busanalyzerbot.cashed.sessions;

import edu.zhuravlev.busanalyzerbot.cashed.cash.SessionCash;
import edu.zhuravlev.busanalyzerbot.controllers.AddBusStopController;
import edu.zhuravlev.busanalyzerbot.controllers.service.AnswersPollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

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

    private AnswersPollService getAnswersPollController() {
        return context.getBean(AnswersPollService.class);
    }

    public void newSessionAddBusStop(Update update) {
        var addBusController = getAddBotController();
        var chatId = update.getMessage().getChatId().toString();

        var sessionMonitor = new Thread(addBusController);
        sessionMonitor.start();

        new DefaultSession(cash, chatId, chatId, addBusController, sessionMonitor);
        addBusController.processUpdate(update);
    }

    public CompletableFuture<Set<String>> newSessionAnswersPoll(Message responseAfterSendPoll) {
        var answersPoll = getAnswersPollController();
        var identifier = responseAfterSendPoll.getPoll().getId();

        var resultForCaller = CompletableFuture.supplyAsync(answersPoll);

        new DefaultSession(cash, identifier, "", answersPoll, resultForCaller);
        return resultForCaller;
    }
}
