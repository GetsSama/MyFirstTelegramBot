package edu.zhuravlev.myfirsttelegrambot;

import busentity.Bus;
import busparser.BusParser;
import busparser.DefaultBusParser;
import edu.zhuravlev.myfirsttelegrambot.illustrator.ScheduleIllustrator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.comparator.Comparators;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class CounterTelegramBot extends TelegramLongPollingBot {
    final BotConfig config;
    final ScheduleIllustrator illustrator;

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(@NonNull Update update) {
        if(update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            String memberName = update.getMessage().getFrom().getFirstName();

            switch (messageText) {
                case "/start":
                    startBot(chatId, memberName);
                    break;
                default:
                    log.info("Unexpected message");
            }
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    private void startBot(long chatId, String userName) {
        BusParser parser = new DefaultBusParser();
        List<Bus> buses = parser.parse(new File(config.getPath()));
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.enableHtml(true);

        buses = buses.stream().sorted((s1, s2) -> Boolean.compare(!s1.isBusOnLive(), !s2.isBusOnLive())).toList();

        String test = illustrator.illustrateAll(buses);

        message.setText(test);
        try {
            execute(message);
            log.info("Reply sent");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}
