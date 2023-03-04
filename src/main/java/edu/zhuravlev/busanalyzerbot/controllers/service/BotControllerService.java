package edu.zhuravlev.busanalyzerbot.controllers.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface BotControllerService<T> {
    T getProcessUpdateResult(Update update);
}
