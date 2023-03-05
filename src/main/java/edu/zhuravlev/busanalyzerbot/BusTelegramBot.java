package edu.zhuravlev.busanalyzerbot;

import edu.zhuravlev.busanalyzerbot.controllers.BotController;
import edu.zhuravlev.busanalyzerbot.router.UpdateRouter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class BusTelegramBot extends TelegramLongPollingBot {
    private final BotConfig config;
    private final UpdateRouter router;

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(@NonNull Update update) {
        router.process(update);
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

}
