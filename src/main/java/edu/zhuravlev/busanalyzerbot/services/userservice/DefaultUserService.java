package edu.zhuravlev.busanalyzerbot.services.userservice;

import edu.zhuravlev.busanalyzerbot.entities.User;
import edu.zhuravlev.busanalyzerbot.mappers.UserMapper;
import edu.zhuravlev.busanalyzerbot.repositories.user.UserTable;
import edu.zhuravlev.busanalyzerbot.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public User getUserById(Long id) {
        UserTable findUserTable = userRepository.findById(id).orElse(new UserTable());
        User user = userMapper.toEntity(findUserTable);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        List<UserTable> findUserTables = (List<UserTable>) userRepository.findAll();
        List<User> users = new ArrayList<>(findUserTables.size());

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
        userMapper.updateUserTable(updatable, user);
        userRepository.save(updatable);
    }
}
