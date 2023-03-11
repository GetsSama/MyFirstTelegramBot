package edu.zhuravlev.busanalyzerbot.controllers;

import edu.zhuravlev.busanalyzerbot.cashed.sessions.SessionService;
import edu.zhuravlev.busanalyzerbot.cashed.sessions.Sessional;
import edu.zhuravlev.busanalyzerbot.entities.BusStop;
import edu.zhuravlev.busanalyzerbot.entities.User;
import edu.zhuravlev.busanalyzerbot.services.busservice.BusStopService;
import edu.zhuravlev.busanalyzerbot.services.userservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component("/delete")
@Scope("prototype")
public class DeleteBusStopController implements BotController, Sessional {
    private AbsSender sender;
    private UserService userService;
    private BusStopService busStopService;
    private Update lastUpdate;
    private String chatId;
    private User user;
    private SessionService sessionService;
    private String deletableBusName;
    private BusStop deletableBusStop;

    @Override
    public synchronized void processUpdate(Update update) {
        if(this.chatId==null)
            this.chatId = update.getMessage().getChatId().toString();
        this.lastUpdate = update;
        notify();
    }

    @Override
    public void run() {
        if(this.chatId==null)
            waitUpdate();
        deleteBusStop();
    }

    @Autowired
    public void setSender(AbsSender sender) {
        this.sender = sender;
    }
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    @Autowired
    public void setBusStopService(BusStopService busStopService) {
        this.busStopService = busStopService;
    }
    @Autowired
    public void setSessionService(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    private void deleteBusStop() {
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
        message.setText("Выберите удаляемую остановку:");
        message.setChatId(chatId);
        message.setReplyMarkup(inlineMarkup);

        var callBackMessage = send(message);
        sessionService.redirectSession(chatId, callBackMessage.getFrom().getId().toString());
        waitUpdate();
        int busStopPosition = Integer.parseInt(lastUpdate.getCallbackQuery().getData());
        deletableBusName = busStopsNames.get(busStopPosition);
        deletableBusStop = user.getBusStops()
                .stream()
                .filter(b -> b.getBusStopName().equals(deletableBusName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User with chat id: " + user.getChatId() + " do not have bus stop with name \"" + deletableBusName + "\""));

        busStopService.deleteBusStop(deletableBusStop);
    }

    private void waitUpdate() {
        try {
            synchronized (this) {
                wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Message send(BotApiMethodMessage method) {
        try {
            return sender.execute(method);
        } catch (TelegramApiException e){
            throw new RuntimeException(e);
        }
    }
}
