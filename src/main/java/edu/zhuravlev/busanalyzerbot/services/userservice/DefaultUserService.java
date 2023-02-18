package edu.zhuravlev.busanalyzerbot.services.userservice;

import edu.zhuravlev.busanalyzerbot.repositories.user.UserTable;
import edu.zhuravlev.busanalyzerbot.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService {
    private final UserRepository userRepository;


    @Override
    public UserTable getUserById(Long id) {
        UserTable findUserTable = userRepository.findById(id).orElse(new UserTable());
        return findUserTable;
    }

    @Override
    public List<UserTable> getAllUsers() {
        List<UserTable> findUserTables = (List<UserTable>) userRepository.findAll();
        return findUserTables;
    }

    @Override
    public void addUser(UserTable userTable) {
        userRepository.save(userTable);
    }
}
