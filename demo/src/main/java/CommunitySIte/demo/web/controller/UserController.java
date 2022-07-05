package CommunitySIte.demo.web.controller;

import CommunitySIte.demo.domain.UserType;
import CommunitySIte.demo.domain.Users;
import CommunitySIte.demo.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotBlank;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/new")
    public String signUp(@ModelAttribute(name = "signUpForm") SignUpForm signUpForm) {
        return "users/signUpForm";
    }

    @PostMapping("/new")
    public String signup(@ModelAttribute @Validated SignUpForm signUpForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "users/signUpForm";
        }

        Users user = new Users();
        user.setUserType(UserType.MEMBER);
        user.setUserName(signUpForm.getUsername());
        user.setLoginId(signUpForm.getLoginId());
        user.setPassword(signUpForm.getPassword());
        userService.join(user);

        return "redirect:/";
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class SignUpForm {
        @NotBlank(message = "아이디를 입력하세요")
        String loginId;
        @NotBlank(message = "이름을 입력하세요")
        String username;
        @NotBlank(message = "비밀번호를 입력하세요")
        String password;
    }
}
