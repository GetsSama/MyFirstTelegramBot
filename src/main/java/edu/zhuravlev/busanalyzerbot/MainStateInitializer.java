package edu.zhuravlev.busanalyzerbot;

import edu.zhuravlev.busanalyzerbot.cashed.sessions.MainSession;
import edu.zhuravlev.busanalyzerbot.cashed.sessions.SessionService;
import edu.zhuravlev.busanalyzerbot.cashed.sessions.Sessional;
import edu.zhuravlev.busanalyzerbot.controllers.BotControllerFactory;
import edu.zhuravlev.busanalyzerbot.controllers.MainStateController;
import edu.zhuravlev.busanalyzerbot.entities.User;
import edu.zhuravlev.busanalyzerbot.services.userservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainStateInitializer {
    private boolean initAll;
    private UserService userService;
    private SessionService sessionService;
    private BotControllerFactory botControllerFactory;
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    @Autowired
    public void setSessionService(SessionService sessionService) {
        this.sessionService = sessionService;
    }
    @Autowired
    public void setBotControllerFactory(BotControllerFactory botControllerFactory) {
        this.botControllerFactory = botControllerFactory;
    }

    public void init() {
        if(!initAll) {
            var users = userService.getAllUsers();
            if(users!=null) {
                users.parallelStream().forEach(this::createMainSession);
                initAll = true;
            }
        }
    }

    public void initNew(User user) {
        createMainSession(user);
    }

    private void createMainSession(User user) {
        var session = (MainSession)sessionService.createSession(MainSession.class);
        var mainController = (MainStateController)botControllerFactory.getBotController("main");
        var controllerThread = new Thread(mainController);

        session.setPrimaryIdentifier(user.getChatId());
        session.setController(mainController);
        session.setJoiningControllerThread(controllerThread);
        var sessionThread = new Thread(session);
        mainController.setChatId(user.getChatId());

        controllerThread.start();
        sessionThread.start();
    }
}
