package by.itransition.backend.controller;

import by.itransition.backend.entity.User;
import by.itransition.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/registration")
public class RegistrationController {
    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> add(@RequestBody User user) {
        if (user.getPassword().isEmpty() && user.getPasswordConfirm().isEmpty()) {
            return new ResponseEntity("redundant param: password can't be empty", HttpStatus.NOT_ACCEPTABLE);
        }
        if (user.getUsername() == null || user.getUsername().trim().length() == 0) {
            return new ResponseEntity("missed param: username", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(userService.addUser(user));

    }

    @GetMapping("/activate/{code}")
    public ResponseEntity<Boolean> activate(@PathVariable String code){
        return ResponseEntity.ok(userService.activateUser(code));
    }
}
