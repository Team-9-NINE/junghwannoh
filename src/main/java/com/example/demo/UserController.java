package com.example.demo;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login_form"; // 템플릿 이름만 매핑 (HTML은 제공하지 않음)
    }

    @GetMapping("/signup")
    public String signupForm(UserSignupForm form) {
        return "signup_form";
    }

    @PostMapping("/signup")
    public String signup(@Valid UserSignupForm form, BindingResult br) {
        if (br.hasErrors()) return "signup_form";
        userService.create(form.getUsername(), form.getEmail(), form.getPassword());
        return "redirect:/user/login";
    }
}
