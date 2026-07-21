package habsida.spring.boot_security.demo.service;

import habsida.spring.boot_security.demo.model.Role;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RoleService {
    List<Role> findAll();
    Role findByRole(String role);
    Optional<Role> findById(Long id);
    Set<Role> findAllByIdIn(Set<Long> ids);
}
