package edu.zhuravlev.busanalyzerbot.services.userservice;

import edu.zhuravlev.busanalyzerbot.entities.User;
import edu.zhuravlev.busanalyzerbot.repositories.user.UserTable;

import java.util.List;

public interface UserService {
    User getUserById(Long id);
    List<User> getAllUsers();
    void addUser(User user);
    User getUserByChatId(long chatId);
}
