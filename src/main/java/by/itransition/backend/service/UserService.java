package by.itransition.backend.service;

import by.itransition.backend.entity.User;
import by.itransition.backend.repo.RoleRepository;
import by.itransition.backend.repo.UserRepository;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.String.format;
import static org.springframework.util.StringUtils.isEmpty;

@Service
@Transactional
public class UserService implements UserDetailsService {

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

    public User addUser(User user) {
        user.setActive(false);
        user.getRoles().add(roleRepository.findByRole("USER"));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        sendMessage(user);
        return userRepository.save(user);
    }

    public void deleteById(Long id){
        userRepository.deleteById(id);
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new LockedException("User not found");
        }
        if (!user.isActive()) {
            throw new LockedException("email not activated");
        }
        return user;
    }


}
