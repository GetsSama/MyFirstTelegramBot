package edu.zhuravlev.busanalyzerbot.services.userservice;

import edu.zhuravlev.busanalyzerbot.entities.BusStop;
import edu.zhuravlev.busanalyzerbot.entities.User;
import edu.zhuravlev.busanalyzerbot.repositories.busstop.BusStopTable;

import java.util.Set;

public interface UserService {
    User getUserById(Long id);
    Set<User> getAllUsers();
    void addUser(User user);
    User getUserByChatId(String chatId);
    void updateUser(User user);
    boolean hasUser(String chatId);
}
