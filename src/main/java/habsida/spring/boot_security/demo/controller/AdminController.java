package habsida.spring.boot_security.demo.controller;

import habsida.spring.boot_security.demo.model.User;
import habsida.spring.boot_security.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class AdminController {
    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin")
    public String adminPage(Authentication authentication, Model model) {
        model.addAttribute("user", authentication.getPrincipal());
        return "admin";
    }

    @GetMapping("/users")
    public String showUsers(Model model) {
        model.addAttribute("users", userService.listUsers());
        return "users";
    }

    @GetMapping("/users/add")
    public String showAddForm(Model model) {
        model.addAttribute("user", new User());
        return "addition-page";
    }

    @PostMapping("/users/add")
    public String saveUser(@Valid @ModelAttribute User user,
                           BindingResult bindingResult){
        if (userService.existsByEmail(user.getEmail())) {
            bindingResult.rejectValue("email", "error.user",
                    "User with email " + user.getEmail() + " already exists");
        }

        if (bindingResult.hasErrors()) {
            return "addition-page";
        }

        userService.add(user);
        return "redirect:/users";
    }

    @PostMapping("/users/delete")
    public String deleteUser(@RequestParam Long id) {
        userService.delete(id);
        return "redirect:/users";
    }

    @GetMapping("/users/edit")
    public String showEditForm(@RequestParam Long id, Model model) {
        User existingUser = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("user", existingUser);
        return "edit-page";
    }


    // добавить изменение пароля и подхватывание пароля из базы
    @PostMapping("/users/edit/{id}")
    public String updateUser(@PathVariable Long id,
                             @Valid @ModelAttribute User user,
                             BindingResult bindingResult,
                             Model model) {
        User userWithSameEmail = userService.findByEmail(user.getEmail());
        if (userWithSameEmail != null && !userWithSameEmail.getId().equals(id)) {
            bindingResult.rejectValue("email", "duplicate",
                    "Email " + user.getEmail() + " is already taken by another user");
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            return "edit-page";
        }

        userService.update(id, user);
        return "redirect:/users";
    }

}
