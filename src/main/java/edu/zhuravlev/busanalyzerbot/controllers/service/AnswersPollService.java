package edu.zhuravlev.busanalyzerbot.controllers.service;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.polls.PollOption;

import java.util.Set;
import java.util.stream.Collectors;

@Component("answerPollService")
public class AnswersPollService implements BotControllerService<Set<String>> {
    private Set<String> parsePriorityBuses(Update update) {
        if(update.hasPoll()) {
            var pollOptions = update.getPoll().getOptions();
            var chosenBuses = pollOptions.stream()
                    .filter(s -> s.getVoterCount() > 0)
                    .map(PollOption::getText)
                    .collect(Collectors.toSet());
            return chosenBuses;
        } else {
            throw new IllegalArgumentException("Update object should contain the Poll object! But given: " + update);
        }
    }

    @Override
    public Set<String> getProcessUpdateResult(Update update) {
        return parsePriorityBuses(update);
    }
}
