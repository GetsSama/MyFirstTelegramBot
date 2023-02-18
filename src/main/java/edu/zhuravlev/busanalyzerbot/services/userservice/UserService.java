package edu.zhuravlev.busanalyzerbot.services.userservice;

import edu.zhuravlev.busanalyzerbot.repositories.user.UserTable;

import java.util.List;

public interface UserService {
    UserTable getUserById(Long id);
    List<UserTable> getAllUsers();
    void addUser(UserTable userTable);
}
