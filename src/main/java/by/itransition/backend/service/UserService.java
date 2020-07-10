package by.itransition.backend.service;

import by.itransition.backend.model.User;
import by.itransition.backend.repo.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.UUID.randomUUID;
import static org.springframework.util.StringUtils.isEmpty;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthService authService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authService = authService;
    }

    public User findById(Long id){
        return userRepository.findById(id).get();
    }

    public List<User> allUsers() {
        return userRepository.findAll();
    }

    public User update(User user, String email, String password) {
        String userEmail = user.getEmail();
        boolean isEmailChanged = validationMail(email, userEmail);
        if (isEmailChanged) {
            user.setEmail(email);
            user.setActive(false);
            if (!isEmpty(email)) {
                user.setActivationCode(randomUUID().toString());
            }
        }
        if (!isEmpty(password)) {
            user.setPassword(passwordEncoder.encode(password));
        }
        if (isEmailChanged) {
            authService.sendMessage(user);
        }
        return userRepository.save(user);
    }

    public void deleteById(Long id){
        userRepository.deleteById(id);
    }

    public User findByUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private boolean validationMail(String email, String userEmail) {
        return email != null && !email.equals(userEmail) || userEmail != null && !userEmail.equals(email);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}
