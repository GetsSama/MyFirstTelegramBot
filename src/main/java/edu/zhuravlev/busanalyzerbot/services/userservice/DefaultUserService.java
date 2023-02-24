package edu.zhuravlev.busanalyzerbot.services.userservice;

import edu.zhuravlev.busanalyzerbot.entities.User;
import edu.zhuravlev.busanalyzerbot.mappers.UserMapper;
import edu.zhuravlev.busanalyzerbot.repositories.user.UserTable;
import edu.zhuravlev.busanalyzerbot.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public User getUserById(Long id) {
        UserTable findUserTable = userRepository.findById(id).orElse(new UserTable());
        return userMapper.toEntity(findUserTable);
    }

    @Override
    public Set<User> getAllUsers() {
        var findUserTables = (Set<UserTable>) userRepository.findAll();
        var users = new HashSet<User>(findUserTables.size());

        for(var user : findUserTables)
            users.add(userMapper.toEntity(user));

        return users;
    }

    @Override
    public void addUser(User user) {
        UserTable addedUser = userMapper.toTable(user);
        userRepository.save(addedUser);
    }

    @Override
    public User getUserByChatId(long chatId) {
        UserTable findUser = userRepository.getUserTableByChatId(chatId);
        return userMapper.toEntity(findUser);
    }

    @Override
    public void updateUser(User user) {
        UserTable updatable = userRepository.getUserTableByChatId(user.getChatId());
        User dbUser = userMapper.toEntity(updatable);

        if(!dbUser.equals(user)) {
            userMapper.updateUserTable(updatable, user);
            userRepository.save(updatable);
        }
    }
}
