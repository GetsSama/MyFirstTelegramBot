package edu.zhuravlev.busanalyzerbot;

import edu.zhuravlev.busanalyzerbot.botcommands.MyCommands;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.List;

@Slf4j
@Component
public class Initializer {
    private TelegramLongPollingBot bot;

    @Autowired
    public void setBot(TelegramLongPollingBot telegramBot) {
        this.bot = telegramBot;
    }

    @EventListener({ContextRefreshedEvent.class})
    public void init() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(bot);
            setCommands(bot);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void setCommands(TelegramLongPollingBot bot) {
        var allCommands = List.of(MyCommands.values());
        var allBotCommands = allCommands.stream().map(MyCommands::getBotCommand).toList();
        var myCommands = new SetMyCommands();

        myCommands.setCommands(allBotCommands);
        try{
            bot.execute(myCommands);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
