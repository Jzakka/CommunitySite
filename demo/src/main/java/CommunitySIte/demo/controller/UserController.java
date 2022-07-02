package CommunitySIte.demo.controller;

import CommunitySIte.demo.domain.Users;
import CommunitySIte.demo.repository.UserRepository;
import CommunitySIte.demo.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/users/login")
    public String  login(@RequestBody LoginForm loginForm) {
        log.info(loginForm.getUsername());
        log.info(loginForm.getPassword());
        return (userService.login(loginForm.getUsername(), loginForm.getPassword()))?"login" : "invalid";
    }

    @PostMapping("/users/new")
    public String  signup(@RequestBody LoginForm loginForm) {
        log.info(loginForm.getUsername());
        log.info(loginForm.getPassword());
        userService.join(new Users(loginForm.getUsername(), loginForm.getPassword()));

        return "redirect:/";
    }



    @PostConstruct
    public void init() {
        Users user1 = new Users();
        user1.setUserName("user1");
        user1.setPassword("1234");

        Users user2 = new Users();
        user2.setUserName("user2");
        user2.setPassword("qwerty");

        userService.join(user1);
        userService.join(user2);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class LoginForm {
        String username;
        String password;
    }
}
