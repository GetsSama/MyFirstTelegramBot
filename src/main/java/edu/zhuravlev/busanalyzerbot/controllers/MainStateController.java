package edu.zhuravlev.busanalyzerbot.controllers;

import busentity.Bus;
import busparser.BusParser;
import edu.zhuravlev.busanalyzerbot.BotConfig;
import edu.zhuravlev.busanalyzerbot.cashed.sessions.Sessional;
import edu.zhuravlev.busanalyzerbot.entities.BusStop;
import edu.zhuravlev.busanalyzerbot.entities.User;
import edu.zhuravlev.busanalyzerbot.illustrator.ScheduleIllustrator;
import edu.zhuravlev.busanalyzerbot.services.userservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component("main")
@Scope("prototype")
public class MainStateController implements BotController, Sessional {
    private AbsSender sender;
    private Update lastUpdate;
    private String chatId;
    private boolean isPaused = false;
    private boolean isModified = true;
    private UserService userService;
    private User user;
    private ScheduleIllustrator illustrator;
    private BusParser parser;
    private BotConfig botConfig;

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    @Autowired
    public void setBotConfig(BotConfig botConfig) {
        this.botConfig = botConfig;
    }

    @Autowired
    public void setParser(BusParser parser) {
        this.parser = parser;
    }

    @Autowired
    public void setIllustrator(ScheduleIllustrator illustrator) {
        this.illustrator = illustrator;
    }

    @Autowired
    public void setSender(AbsSender sender) {
        this.sender = sender;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public synchronized void processUpdate(Update update) {
        this.lastUpdate = update;
        notify();
    }

    public synchronized void pause() {
        isPaused = true;
        notify();
    }

    public synchronized void resume() {
        isPaused = false;
        isModified = true;
        notify();
    }

    @Override
    public void run() {
        try {
            while(true) {
                if(isPaused) {
                    hideButtons("Edit mode");
                    synchronized (this) {
                        wait();
                    }
                    continue;
                }
                if(isModified) {
                    //hideButtons();
                    refreshUser();
                    showButtons();
                    isModified = false;
                    synchronized (this) {
                        wait();
                    }
                    continue;
                }
                processButton(lastUpdate);
                synchronized (this) {
                    wait();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void refreshUser() {
        this.user = userService.getUserByChatId(chatId);
    }

    private void showButtons() {
        int numberOfButtons = user.getBusStops().size();
        if (numberOfButtons>0) {
            var buttons = new ArrayList<KeyboardRow>(numberOfButtons / 3 + 1);
            var busIterator = user.getBusStops().iterator();

            while (busIterator.hasNext()) {
                var oneLineButtons = new ArrayList<KeyboardButton>(3);
                for (int i = 0; i < 3; i++) {
                    if (busIterator.hasNext()) {
                        var button = new KeyboardButton();
                        button.setText(busIterator.next().getBusStopName());
                        oneLineButtons.add(button);
                    }
                }
                buttons.add(new KeyboardRow(oneLineButtons));
            }

            var keyboard = new ReplyKeyboardMarkup();
            var message = new SendMessage();
            keyboard.setKeyboard(buttons);
            keyboard.setIsPersistent(true);
            message.setText("Choose:");
            message.setChatId(chatId);
            message.setReplyMarkup(keyboard);

            try {
                sender.execute(message);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void hideButtons(String text) {
        var hideKeyboard = new ReplyKeyboardRemove();
        hideKeyboard.setRemoveKeyboard(true);

        var message = new SendMessage();
        message.setText(text);
        message.setChatId(chatId);
        message.setReplyMarkup(hideKeyboard);

        try{
            sender.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void processButton(Update update) {
        var busStopName = update.getMessage().getText();
        BusStop chosenBusStop = null;
        for(var busStop : user.getBusStops()) {
            if(busStop.getBusStopName().equals(busStopName))
                chosenBusStop = busStop;
        }
        if(chosenBusStop==null)
            throw new RuntimeException("No bus stops with name - " + busStopName);

        var priorityBusesName = chosenBusStop.getPriorityBuses();
        List<Bus> allBuses;
        if(botConfig.isDebugMode())
            allBuses = parser.parse(new File(botConfig.getPath()));
        else
            allBuses = parser.parse(chosenBusStop.getBusStopUrl());

        var messagingBuses = allBuses
                .stream()
                .filter(b -> priorityBusesName.contains(b.getBusName()))
                .sorted(Comparator.comparing(Bus::getArrivedTime))
                .toList();

        var formatMessage = illustrator.illustrateAll(messagingBuses);
        var message = new SendMessage();
        message.setChatId(chatId);
        message.setText(formatMessage);
        message.setParseMode("HTML");

        try {
            sender.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
