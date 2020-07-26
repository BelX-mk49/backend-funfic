package by.itransition.backend.controller;

import by.itransition.backend.exceptions.UserNotActivatedException;
import by.itransition.backend.model.User;
import by.itransition.backend.security.jwt.JwtTokenProvider;
import by.itransition.backend.service.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static org.springframework.http.HttpStatus.OK;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final JwtTokenProvider tokenProvider;
    private final UserServiceImpl userService;

    public AuthController(JwtTokenProvider tokenProvider, UserServiceImpl userService) {
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }

    @GetMapping("/activate/{code}")
    public ResponseEntity<String> activate(@PathVariable String code){
        userService.activateUser(code);
        return new ResponseEntity<>("Account Activated Successfully", OK);
    }

    @GetMapping("/login")
    public ResponseEntity<?> login(Principal principal) {
        if (principal == null) {
            return ResponseEntity.ok(principal);
        }
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) principal;
        User user = userService.findByUsername(authenticationToken.getName());
        if (!user.isActive()) {
            throw new UserNotActivatedException("User " + user.getUsername() + " was not activated");
        }
        user.setToken(tokenProvider.generateToken(authenticationToken));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userService.findByUsername(user.getUsername()) != null || userService.findByEmail(user.getEmail())!= null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new  ResponseEntity<>(userService.saveUser(user), HttpStatus.CREATED);
    }
}
