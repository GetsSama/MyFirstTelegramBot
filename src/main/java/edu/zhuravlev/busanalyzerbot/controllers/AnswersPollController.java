package edu.zhuravlev.busanalyzerbot.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.polls.PollOption;

import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
@Scope("prototype")
@Slf4j
public class AnswersPollController implements BotController, Supplier<Set<String>> {
    private Update update;

    @Override
    public synchronized void processUpdate(Update update) {
        this.update = update;
        notify();
    }

    @Override
    public Set<String> get() {
        log.info(Thread.currentThread().getName() + " started!");
        Set<String> result;
        if(this.update == null) {
            try {
                synchronized (this) {
                    log.info(Thread.currentThread().getName() + " waiting!");
                    wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        log.info(Thread.currentThread().getName() + " resumed!");
        result = parsePriorityBuses(update);
        return result;
    }

    private Set<String> parsePriorityBuses(Update update) {
        var pollOptions = update.getPoll().getOptions();
        var chosenBuses = pollOptions.stream()
                .filter(s -> s.getVoterCount()>0)
                .map(PollOption::getText)
                .collect(Collectors.toSet());
        return chosenBuses;
    }
}
