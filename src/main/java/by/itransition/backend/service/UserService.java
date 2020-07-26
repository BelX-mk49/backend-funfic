package by.itransition.backend.service;

import by.itransition.backend.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    User saveUser(User user);

    void activateUser(String code);

    User updateUser(User user);

    void deleteUser(Long userId);

    User findByUsername(String username);

    User findByUserId(Long userId);

    User findByEmail(String email);

    List<User> findAllUsers();

    Long numberOfUsers();
}
