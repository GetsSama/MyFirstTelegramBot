package edu.zhuravlev.busanalyzerbot.controllers;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Set;
import java.util.concurrent.Callable;

@Component
@Scope("prototype")
public class AnswersPollController implements BotController, Callable<Set<String>> {
    private Update update;

    @Override
    public synchronized void processUpdate(Update update) {
        this.update = update;
        notify();
    }

    @Override
    public Set<String> call() throws Exception {
        Set<String> result;
        if(this.update == null) {
            try {
                synchronized (this) {
                    wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        result = parsePriorityBuses(update);
        return result;
    }

    private Set<String> parsePriorityBuses(Update update) {
        return null;
    }
}
