package edu.zhuravlev.busanalyzerbot;

import edu.zhuravlev.busanalyzerbot.controllers.BotController;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class BusTelegramBot extends TelegramLongPollingBot {
    private final BotConfig config;
    private final BotController controller;

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(@NonNull Update update) {
        controller.processUpdate(update);
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }
}
