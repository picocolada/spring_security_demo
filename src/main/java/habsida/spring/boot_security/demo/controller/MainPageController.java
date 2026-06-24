package habsida.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MainPageController {

    @GetMapping(value = "/")
    public String welcomePage() {
        return "redirect:/users";
    }
}
