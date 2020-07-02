package by.itransition.backend.controller;

import by.itransition.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/registration")
public class RegistrationController {
    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/activate/{code}")
    public ResponseEntity<Boolean> activate(@PathVariable String code){
        return ResponseEntity.ok(userService.activateUser(code));
    }
}
