package habsida.spring.boot_security.demo.service;

import habsida.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void add(User user);
    void delete(Long id);
    User findByEmail(String email);
    List<User> listUsers();
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    void update(Long id, User user);

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
