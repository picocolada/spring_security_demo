package habsida.spring.boot_security.demo.dto;

import habsida.spring.boot_security.demo.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

public class UserDto {

    private Long id;

    private String username;
    //добавить сет ролей

    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    @NotBlank(message = "Please provide your first name")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Name must contain only letters")
    private String firstName;

    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    @NotBlank(message = "Please provide your last name")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Name must contain only letters")
    private String lastName;

    @Email(message = "Please provide a valid email address")
    @Size(min = 6, max = 80, message = "Email must be between 6 and 80 characters")
    @NotBlank(message = "Email is required")
    private String email;

    private String password;

    private Set<Long> roleIds = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Set<Long> roleIds) {
        this.roleIds = roleIds;
    }
}
