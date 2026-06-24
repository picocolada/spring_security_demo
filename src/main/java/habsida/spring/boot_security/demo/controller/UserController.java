package habsida.spring.boot_security.demo.controller;


import habsida.spring.boot_security.demo.model.User;
import habsida.spring.boot_security.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showUsers(Model model){
        model.addAttribute("users", userService.listUsers());
        return "users";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("user", new User());
        return "addition-page";
    }

    @PostMapping("/add")
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

    @PostMapping("/delete")
    public String deleteUser(@RequestParam Long id) {
        userService.delete(id);
        return "redirect:/users";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam Long id, Model model) {
        User existingUser = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("user", existingUser);
        return "edit-page";
    }

    @PostMapping("/edit/{id}")
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
