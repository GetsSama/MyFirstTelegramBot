package edu.zhuravlev.busanalyzerbot.router;

import edu.zhuravlev.busanalyzerbot.botcommands.MyCommands;
import edu.zhuravlev.busanalyzerbot.cashed.sessions.DefaultSession;
import edu.zhuravlev.busanalyzerbot.cashed.sessions.SessionService;
import edu.zhuravlev.busanalyzerbot.cashed.sessions.Sessional;
import edu.zhuravlev.busanalyzerbot.controllers.BotControllerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class DefaultUpdateRouter implements UpdateRouter{
    private BotControllerFactory controllerFactory;
    private SessionService sessionService;
    private final Set<String> commands;

    public DefaultUpdateRouter() {
        var botCommands = Arrays.asList(MyCommands.values());
        this.commands = botCommands.stream()
                .map(MyCommands::getBotCommand)
                .map(BotCommand::getCommand).collect(Collectors.toUnmodifiableSet());
    }

    @Autowired
    public void setControllerFactory(BotControllerFactory controllerFactory) {
        this.controllerFactory = controllerFactory;
    }

    @Autowired
    public void setSessionFactory(SessionService sessionService) {
        this.sessionService = sessionService;
    }


    @Override
    public void process(Update update) {
        log.info("Update: " + update);
        if(update.hasMessage()) {
            String message = update.getMessage().getText();

            if(isBotCommand(message)) {
                var botController = controllerFactory.getBotController(message);

                if(botController instanceof Sessional) {
                    var session = (DefaultSession) sessionService.createSession(DefaultSession.class);
                    var controllerThread = new Thread((Sessional)botController);
                    session.setPrimaryIdentifier(getIdentifierFromUpdate(update));
                    session.setController(botController);
                    session.setJoiningControllerThread(controllerThread);
                    var sessionThread = new Thread(session);

                    controllerThread.start();
                    sessionThread.start();
                    while(controllerThread.getState() != Thread.State.WAITING) {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                botController.processUpdate(update);
                return;
            }
        }
        var botControllerFromId = sessionService.getSessionById(getIdentifierFromUpdate(update));
        try {
            botControllerFromId.getController().processUpdate(update);
        } catch (NullPointerException e) {
            log.error("Unsupported command in this context throw exception: " + e);
        }
    }

    private String getIdentifierFromUpdate(Update update) {
        if(update.hasMessage())
            return update.getMessage().getChatId().toString();
        if(update.hasPoll())
            return update.getPoll().getId();
        if(update.hasCallbackQuery())
            return update.getCallbackQuery().getFrom().getId().toString();
        else
            throw new UnsupportedOperationException("This version supports only Updates with Message or Poll.");
    }

    private boolean isBotCommand(String text) {
        return commands.contains(text);
    }
}
