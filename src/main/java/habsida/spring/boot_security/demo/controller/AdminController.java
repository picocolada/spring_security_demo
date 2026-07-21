package habsida.spring.boot_security.demo.controller;

import habsida.spring.boot_security.demo.dto.UserDto;
import habsida.spring.boot_security.demo.model.Role;
import habsida.spring.boot_security.demo.model.User;
import habsida.spring.boot_security.demo.service.RoleService;
import habsida.spring.boot_security.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;


@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }


    @GetMapping
    public String adminPage(Authentication authentication, Model model) {
        model.addAttribute("user", authentication.getPrincipal());
        model.addAttribute("users", userService.listUsers());
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
        model.addAttribute("allRoles", roleService.findAll());
        return "addition-page";
    }

    @PostMapping("/users/add")
    public String saveUser(@Valid @ModelAttribute User user,
                           BindingResult bindingResult,
                           Model model){
        if (userService.existsByEmail(user.getEmail())) {
            bindingResult.rejectValue("email", "error.user",
                    "User with email " + user.getEmail() + " already exists");
        }

        if (userService.existsByUsername(user.getUsername())) {
            bindingResult.rejectValue("username", "error.user",
                    "User with username " + user.getUsername() + " already exists");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("allRoles", roleService.findAll());
            return "addition-page";
        }

        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            Role defaultRole = roleService.findByRole("ROLE_USER");
            if (defaultRole != null) {
                user.setRoles(Set.of(defaultRole));
            }
        }

        userService.add(user);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/delete")
    public String deleteUser(@RequestParam Long id) {
        userService.delete(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/users/edit")
    public String showEditForm(@RequestParam Long id, Model model) {
        User existingUser = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDto userDto = new UserDto();
        userDto.setId(existingUser.getId());
        userDto.setUsername(existingUser.getUsername());
        userDto.setFirstName(existingUser.getFirstName());
        userDto.setLastName(existingUser.getLastName());
        userDto.setEmail(existingUser.getEmail());

        Set<Long> roleIds = new HashSet<>();
        for (Role role : existingUser.getRoles()) {
            roleIds.add(role.getId());
        }
        userDto.setRoleIds(roleIds);

        model.addAttribute("allRoles", roleService.findAll());
        model.addAttribute("user", userDto);
        return "edit-page";
    }


    @PostMapping("/users/edit/{id}")
    public String updateUser(@PathVariable Long id,
                             @Valid @ModelAttribute("user") UserDto userDto,
                             BindingResult bindingResult,
                             Model model) {
        User existingUser = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User userWithSameEmail = userService.findByEmail(userDto.getEmail());
        if (userWithSameEmail != null && !userWithSameEmail.getId().equals(id)) {
            bindingResult.rejectValue("email", "duplicate",
                    "Email " + userDto.getEmail() + " is already taken by another user");
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", userDto);
            model.addAttribute("allRoles", roleService.findAll());
            return "edit-page";
        }

        User user = new User();
        user.setId(userDto.getId());
        user.setUsername(existingUser.getUsername());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());

        if (userDto.getRoleIds() != null && !userDto.getRoleIds().isEmpty()) {
            Set<Role> roles = roleService.findAllByIdIn(userDto.getRoleIds());
            user.setRoles(roles);
        } else {
            user.setRoles(existingUser.getRoles());
        }

        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            user.setPassword(userDto.getPassword());
        }

        userService.update(id, user);
        return "redirect:/admin/users";
    }

}
