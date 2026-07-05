package habsida.spring.boot_security.demo.controller;


import habsida.spring.boot_security.demo.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //юзер должен видеть инфо только о себе

    @GetMapping("/user")
    public String userPage(Authentication authentication, Model model) {
        model.addAttribute("user", authentication.getPrincipal());
        return "user";
    }



}
