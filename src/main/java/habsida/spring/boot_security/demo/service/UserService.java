package habsida.spring.boot_security.demo.service;

import habsida.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void add(User user);
    boolean existsByEmail(String email);
    void delete(Long id);
    User findByEmail(String email);
    List<User> listUsers();
    Optional<User> findById(Long id);
    void update(Long id, User user);
}
