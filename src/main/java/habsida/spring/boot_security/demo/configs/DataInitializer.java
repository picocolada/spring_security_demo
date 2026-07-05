package habsida.spring.boot_security.demo.configs;

import habsida.spring.boot_security.demo.model.Role;
import habsida.spring.boot_security.demo.model.User;
import habsida.spring.boot_security.demo.repository.RoleRepository;
import habsida.spring.boot_security.demo.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    @Transactional
    public void createTestAdminUser() {
        Role roleUser = roleRepository.findByRole("USER")
                .orElseGet(() -> {
                    Role newRole = new Role("USER");
                    return roleRepository.save(newRole);
                });

        Role roleAdmin = roleRepository.findByRole("ADMIN")
                .orElseGet(() -> {
                    Role newRole = new Role("ADMIN");
                    return roleRepository.save(newRole);
                });

        if (userRepository.findByEmail("user@test.com").isEmpty()) {
            User user = new User();
            user.setFirstName("User");
            user.setLastName("UserLast");
            user.setEmail("user@test.com");
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user"));
            user.setRoles(Set.of(roleUser));
            userRepository.save(user);
        }


        if (userRepository.findByEmail("admin@test.com").isEmpty()) {
            User admin = new User();
            admin.setFirstName("Admin");
            admin.setLastName("AdminLast");
            admin.setEmail("admin@test.com");
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRoles(Set.of(roleAdmin, roleUser));
            userRepository.save(admin);
        }
    }
}
