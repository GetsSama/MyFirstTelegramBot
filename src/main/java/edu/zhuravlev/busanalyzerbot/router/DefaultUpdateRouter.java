package edu.zhuravlev.busanalyzerbot.router;

import edu.zhuravlev.busanalyzerbot.botcommands.MyCommands;
import edu.zhuravlev.busanalyzerbot.cashed.sessions.DefaultSession;
import edu.zhuravlev.busanalyzerbot.cashed.sessions.SessionFactory;
import edu.zhuravlev.busanalyzerbot.cashed.sessions.Sessional;
import edu.zhuravlev.busanalyzerbot.controllers.BotControllerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DefaultUpdateRouter implements UpdateRouter{
    private BotControllerFactory controllerFactory;
    private SessionFactory sessionFactory;
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
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public void process(Update update) {
        if(update.hasMessage()) {
            String message = update.getMessage().getText();
            if(isBotCommand(message)) {
                var botController = controllerFactory.getBotController(message);
                if(botController instanceof Sessional) {
                    var session = sessionFactory.createSession(DefaultSession.class);
                    var controllerThread = new Thread((Runnable)botController);

                }
            }
        }
    }

    @Override
    public void redirect(String from, String to) {

    }

    private String getIdentifierFromUpdate(Update update) {
        if(update.hasMessage())
            return update.getMessage().getChatId().toString();
        if(update.hasPoll())
            return update.getPoll().getId();
        else
            throw new UnsupportedOperationException("This version supports only Updates with Message or Poll.");
    }

    private boolean isBotCommand(String text) {
        return commands.contains(text);
    }
}
