package by.itransition.backend.service;

import by.itransition.backend.model.ERole;
import by.itransition.backend.model.Role;
import by.itransition.backend.model.User;
import by.itransition.backend.repo.RoleRepository;
import by.itransition.backend.repo.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.String.format;
import static java.util.UUID.randomUUID;
import static org.springframework.util.StringUtils.isEmpty;

@Service
@Transactional
public class UserService {

    public static final String ACTIVATE_ACCOUNT_MESSAGE = "Hello, Admin! \n" +
            "Someone with username %s and email %s want to registration in Learn management system." +
            "Please visit next link to activate this account: http://localhost:8080/activate/%s";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailSender mailSender;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, MailSender mailSender, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
        this.roleRepository = roleRepository;
    }

    public User findById(Long id){
        return userRepository.findById(id).get();
    }

    public List<User> allUsers() {
        return userRepository.findAll();
    }

    public User add(User user) {
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(userRole);
        user.setActive(false);
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        sendMessage(user);
        return userRepository.save(user);
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
            sendMessage(user);
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

    private void sendMessage(User user) {
        if (!isEmpty(user.getEmail())) {
            String message = format(ACTIVATE_ACCOUNT_MESSAGE,
                    user.getUsername(),
                    user.getEmail(),
                    user.getActivationCode()
            );
            mailSender.send(user.getEmail(), "Activation account", message);
        }
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public boolean activateUser(String code) {
        User user = userRepository.findByActivationCode(code);
        if (user != null) {
            user.setActive(true);
            userRepository.save(user);
            return TRUE;
        }else {
            return FALSE;
        }
    }

    private boolean validationMail(String email, String userEmail) {
        return email != null && !email.equals(userEmail) || userEmail != null && !userEmail.equals(email);
    }


}
