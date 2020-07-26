package by.itransition.backend.service;

import by.itransition.backend.model.Role;
import by.itransition.backend.model.User;
import by.itransition.backend.repository.UserRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

import static java.lang.String.format;
import static java.util.UUID.randomUUID;
import static org.springframework.util.StringUtils.isEmpty;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final MailSender mailSender;

    public static final String ACTIVATE_ACCOUNT_MESSAGE = "Hello, %s! \n " +
            "Thank you for registering on Fanfic" +
            "To activate your, please click on the following link (this will confirm your email address) \n" +
            "https://backend-funfic.herokuapp.com/api/auth/activate/%s \n" +
            "Thank You";

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder encoder, MailSender mailSender) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.mailSender = mailSender;
    }

    @Override
    public User saveUser(User user) {
        user.setUsername(user.getUsername());
        user.setEmail(user.getEmail());
        user.setActive(false);
        user.setCreated(Instant.now());
        user.setActivationCode(randomUUID().toString());
        user.setRole(Role.USER);
        user.setPassword(encoder.encode(user.getPassword()));
        sendMessage(user);
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        boolean isEmailChanged = changingEmail(user);
        if (!isEmpty(user.getPassword())) {
            user.setPassword(encoder.encode(user.getPassword()));
        }
        userRepository.save(user);
        if (isEmailChanged) {
            sendMessage(user);
        }
        return userRepository.save(user);
    }

    private boolean changingEmail(User user) {
        String newEmail = user.getEmail();
        boolean isEmailChanged = validationMail(newEmail, (userRepository.findByUsername(user.getUsername())).get().getEmail());
        if (isEmailChanged) {
            user.setEmail(newEmail);
            user.setActive(false);
            if (!isEmpty(newEmail)) {
                user.setActivationCode(randomUUID().toString());
            }
        }
        return isEmailChanged;
    }

    @Override
    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

    @Override
    public User findByUsername(String username){
        return userRepository.findByUsername(username)
                .orElse(null);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElse(null);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Long numberOfUsers() {
        return userRepository.count();
    }

    @Override
    public User findByUserId(Long id){
        return userRepository.findById(id).get();
    }

    @Override
    public void activateUser(String code) {
        User user = userRepository.findByActivationCode(code);
        if (user != null) {
            user.setActive(true);
            userRepository.save(user);
        }
    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + authentication.getName()));
    }

    public void sendMessage(User user) {
        if (!isEmpty(user.getEmail())) {
            String message = format(ACTIVATE_ACCOUNT_MESSAGE,
                    user.getUsername(),
                    user.getActivationCode()
            );
            mailSender.send(user.getEmail(), "Activation account", message);
        }
    }

    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }

    private boolean validationMail(String email, String userEmail) {
        return email != null && !email.equals(userEmail) || userEmail != null && !userEmail.equals(email);
    }
}
