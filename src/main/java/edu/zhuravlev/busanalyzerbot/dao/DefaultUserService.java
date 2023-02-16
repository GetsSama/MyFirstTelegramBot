package edu.zhuravlev.busanalyzerbot.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService {
    private final UserRepository userRepository;


    @Override
    public User getUserById(Long id) {
        User findUser = userRepository.findById(id).orElse(new User());
        return findUser;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> findUsers = (List<User>) userRepository.findAll();
        return findUsers;
    }

    @Override
    public void addUser(User user) {
        userRepository.save(user);
    }
}
