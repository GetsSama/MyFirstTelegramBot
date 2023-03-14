package edu.zhuravlev.busanalyzerbot.controllers;

import edu.zhuravlev.busanalyzerbot.entities.BusStop;
import edu.zhuravlev.busanalyzerbot.entities.User;
import edu.zhuravlev.busanalyzerbot.services.userservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component("/delete")
@Scope("prototype")
public class DeleteBusStopController extends AbstractSessionalBotController {
    private UserService userService;
    private User user;

    @Override
    public void run() {
        if(this.chatId==null)
            waitUpdate();
        deleteBusStop();
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
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
        BusStop deletableBusStop = user.getBusStops()
                .stream()
                .filter(b -> b.getBusStopName().equals(busStopsNames.get(busStopPosition)))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User with chat id: " + user.getChatId() + " do not have bus stop with name \"" + busStopsNames.get(busStopPosition) + "\""));

        user.getBusStops().remove(deletableBusStop);
        userService.updateUser(user);
    }
}
