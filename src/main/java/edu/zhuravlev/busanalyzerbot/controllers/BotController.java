package edu.zhuravlev.busanalyzerbot.controllers;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface BotController {
    void processUpdate(Update update);
}
