package edu.zhuravlev.busanalyzerbot.dao;

import java.util.List;

public interface UserService {
    User getUserById(Long id);
    List<User> getAllUsers();
    void addUser(User user);
}
