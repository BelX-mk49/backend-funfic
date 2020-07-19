package by.itransition.backend.service;

import by.itransition.backend.model.ERole;
import by.itransition.backend.model.Role;
import by.itransition.backend.model.User;
import by.itransition.backend.payload.request.LoginRequest;
import by.itransition.backend.payload.request.RefreshTokenRequest;
import by.itransition.backend.payload.request.SignupRequest;
import by.itransition.backend.payload.resposne.AuthenticationResponse;
import by.itransition.backend.payload.resposne.MessageResponse;
import by.itransition.backend.repo.RoleRepository;
import by.itransition.backend.repo.UserRepository;
import by.itransition.backend.security.jwt.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import static java.lang.String.format;
import static java.util.UUID.randomUUID;
import static org.springframework.util.StringUtils.isEmpty;

@Service
@Transactional
@AllArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final MailSender mailSender;
    private final RefreshTokenService refreshTokenService;

    public static final String ACTIVATE_ACCOUNT_MESSAGE = "Hello, %s! \n " +
            "Thank you for registering on Fanfic" +
            "To activate your, please click on the following link (this will confirm your email address) \n" +
            "http://localhost:8080/api/auth/activate/%s \n" +
            "Thank You";

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtUtils.generateJwtToken(authenticate);
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtUtils.getJwtExpirationMs()))
                .username(loginRequest.getUsername())
                .build();
    }

    public ResponseEntity<?> signup (SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }
        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setEmail(signupRequest.getEmail());
        user.setActive(false);
        user.setCreated(Instant.now());
        user.setActivationCode(randomUUID().toString());
        Set<Role> roles = getRoles(signupRequest);
        user.setRoles(roles);
        user.setPassword(encoder.encode(signupRequest.getPassword()));
        userRepository.save(user);
        sendMessage(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtUtils.generateTokenWithUserName(refreshTokenRequest.getUsername());
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtUtils.getJwtExpirationMs()))
                .username(refreshTokenRequest.getUsername())
                .build();
    }

    public void activateUser(String code) {
        User user = userRepository.findByActivationCode(code);
        if (user != null) {
            user.setActive(true);
            userRepository.save(user);
        }
    }

    private boolean validationMail(String email, String userEmail) {
        return email != null && !email.equals(userEmail) || userEmail != null && !userEmail.equals(email);
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

    private Set<Role> getRoles(SignupRequest signupRequest) {
        Set<String> strRoles = signupRequest.getRole();
        Set<Role> roles = new HashSet<>();
        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin" -> {
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                    }
                    default -> {
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                    }
                }
            });
        }
        return roles;
    }

    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }
}
