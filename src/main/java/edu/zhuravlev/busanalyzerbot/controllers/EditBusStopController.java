package edu.zhuravlev.busanalyzerbot.controllers;

import busentity.Bus;
import busparser.BusParser;
import edu.zhuravlev.busanalyzerbot.BotConfig;
import edu.zhuravlev.busanalyzerbot.controllers.service.BotControllerService;
import edu.zhuravlev.busanalyzerbot.entities.BusStop;
import edu.zhuravlev.busanalyzerbot.entities.User;
import edu.zhuravlev.busanalyzerbot.services.userservice.UserService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Component("/edit_bus_stop")
@Scope("prototype")
@Slf4j
public class EditBusStopController extends AbstractSessionalBotController {
    private BusParser parser;
    private EditControllerState state;
    private User user;
    private UserService userService;
    private BotConfig config;
    private String editableBusName;
    private String editableFeature;
    private BusStop editableBusStop;
    private boolean onProcess;
    private BotControllerService<Set<String>> botControllerService;
    private static final String[] features = {"Название остановки", "Отслеживаемые автобусы"};

    private final Map<String, Runnable> featuresMethods = new HashMap<>();

    public EditBusStopController() {
        super();
        this.state = EditControllerState.CHOOSE_EDIT_BUSSTOP;
    }

    @Autowired
    @Qualifier("answerPollService")
    public void setBotControllerService(BotControllerService<Set<String>> botControllerService) {
        this.botControllerService = botControllerService;
    }
    @Autowired
    public void setConfig(BotConfig config) {
        this.config = config;
    }
    @Autowired
    public void setParser(BusParser parser) {
        this.parser = parser;
    }
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run() {
        log.info("Editor start.");
        onProcess = true;
        waitUpdate();
        while (onProcess) {
            switch (state) {
                case CHOOSE_EDIT_BUSSTOP -> {
                    choseEditableBusStop();
                    state = EditControllerState.CHOOSE_EDIT_FEATURE;
                    waitUpdate();
                }
                case CHOOSE_EDIT_FEATURE -> {
                    choseEditableFeature();
                    state = EditControllerState.NEW_VALUE;
                }
                case NEW_VALUE -> {
                    getNewValue();
                    state = EditControllerState.UPDATE;
                }
                case UPDATE -> {
                    saveUpdate();
                    onProcess = false;
                    log.info("Editor complete");
                }
            }
        }
    }

    private void choseEditableBusStop() {
        this.user = userService.getUserByChatId(chatId);
        var busStopsNames = user.getBusStops().stream().map(BusStop::getBusStopName).toList();
        var inlineButtons = new ArrayList<List<InlineKeyboardButton>>();

        var busStopNameIter = busStopsNames.iterator();
        int lineCounter = 1;
        while (busStopNameIter.hasNext()) {
            var oneLineButtons = new ArrayList<InlineKeyboardButton>(3);
            for (int i=0; i<3; i++) {
                if(busStopNameIter.hasNext()) {
                    var button = new InlineKeyboardButton();
                    button.setText(busStopNameIter.next());
                    button.setCallbackData(String.valueOf(lineCounter * i));
                    oneLineButtons.add(button);
                } else
                    break;
            }
            inlineButtons.add(oneLineButtons);
            lineCounter++;
        }

        var inlineMarkup = new InlineKeyboardMarkup();
        inlineMarkup.setKeyboard(inlineButtons);
        var message = new SendMessage();
        message.setText("Выберите редактируемую остановку:");
        message.setChatId(chatId);
        message.setReplyMarkup(inlineMarkup);

        var callBackMessage = send(message);
        sessionService.redirectSession(chatId, callBackMessage.getFrom().getId().toString());
        waitUpdate();
        int busStopPosition = Integer.parseInt(lastUpdate.getCallbackQuery().getData());
        editableBusName = busStopsNames.get(busStopPosition);
        editableBusStop = user.getBusStops()
                .stream()
                .filter(b -> b.getBusStopName().equals(editableBusName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User with chat id: " + user.getChatId() + " do not have bus stop with name \"" + editableBusName + "\""));
    }

    private void choseEditableFeature() {
        var inlineButtons = new ArrayList<List<InlineKeyboardButton>>(features.length);

        for(int i=0; i< features.length; i++) {
            var button = new InlineKeyboardButton();
            button.setText(features[i]);
            button.setCallbackData(String.valueOf(i));
            inlineButtons.add(List.of(button));
        }

        var inlineMarkup = new InlineKeyboardMarkup();
        inlineMarkup.setKeyboard(inlineButtons);

        var message = new SendMessage();
        message.setText("Выберите редактируемое свойство:");
        message.setChatId(chatId);
        message.setReplyMarkup(inlineMarkup);

        var callBackMessage = send(message);
        sessionService.redirectSession(chatId, callBackMessage.getFrom().getId().toString());
        waitUpdate();
        int callBackData = Integer.parseInt(lastUpdate.getCallbackQuery().getData());
        editableFeature = features[callBackData];
    }

    private void getNewValue() {
        var runnableMethod = featuresMethods.get(editableFeature);
        runnableMethod.run();
    }

    private void saveUpdate() {
        userService.updateUser(user);
    }

    private enum EditControllerState {
        CHOOSE_EDIT_BUSSTOP, CHOOSE_EDIT_FEATURE, NEW_VALUE, UPDATE;
    }

    @PostConstruct
    private void init() {
        featuresMethods.put(features[0], this::changeBusStopName);
        featuresMethods.put(features[1], this::changePriorityBuses);
    }

    private void changeBusStopName() {
        sendSimpleMessage("Введите новое название для остановки");
        waitUpdate();
        var currentBusStopsNames = user.getBusStops().stream().map(BusStop::getBusStopName).collect(Collectors.toSet());

        while(currentBusStopsNames.contains(lastUpdate.getMessage().getText())) {
            sendSimpleMessage("Остановка с таким названием уже существует!");
            waitUpdate();
        }

        //busStopService.deleteBusStop(editableBusStop);
        editableBusStop.setBusStopName(lastUpdate.getMessage().getText());
        userService.updateUser(user);
    }

    private void changePriorityBuses() {
        List<Bus> allBuses;
        if(config.isDebugMode())
            allBuses = parser.parse(new File(config.getPath()));
        else
            allBuses = parser.parse(editableBusStop.getBusStopUrl());

        var allBusesNames = allBuses.stream().map(Bus::getBusName).toList();
        var poll = new SendPoll();
        poll.setQuestion("Выберите автобусы для отслеживания:");
        poll.setChatId(chatId);
        poll.setOptions(allBusesNames);
        poll.setAllowMultipleAnswers(true);

        var returnedMessage = send(poll);
        sessionService.redirectSession(chatId, returnedMessage.getPoll().getId());
        waitUpdate();
        var newPriorityBuses = botControllerService.getProcessUpdateResult(lastUpdate);

        editableBusStop.setPriorityBuses(newPriorityBuses);
        //busStopService.updateBusStop(editableBusStop);
        userService.updateUser(user);
    }
}
