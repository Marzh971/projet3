package fr.spam.backbookhub.features.connection.user.seeders;

import fr.spam.backbookhub.features.connection.role.models.Role;
import fr.spam.backbookhub.features.connection.role.services.IRoleService;
import fr.spam.backbookhub.features.connection.user.models.User;
import fr.spam.backbookhub.features.connection.user.services.IUserService;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(2)
public class UserSeeder {

    private final IUserService userService;
    private final IRoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public UserSeeder(IUserService userService, IRoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    public void seed() {
        Role userRole = roleService.findByLabel("USER").getData();
        Role librarianRole = roleService.findByLabel("LIBRARIAN").getData();
        Role adminRole = roleService.findByLabel("ADMIN").getData();

        seedUserIfAbsent("user", "user@bookhub.fr", "0600000001", List.of(userRole));
        seedUserIfAbsent("librarian", "librarian@bookhub.fr", "0600000002", List.of(userRole, librarianRole));
        seedUserIfAbsent("admin", "admin@bookhub.fr", "0600000003", List.of(userRole, librarianRole, adminRole));
    }

    private void seedUserIfAbsent(String username, String email, String phone, List<Role> roles) {
        var existing = userService.findByPseudo(username);
        if (existing.getData() != null) {
            return;
        }

        User user = User.builder()
                .username(username)
                .firstName("Prenom")
                .lastName("Nom")
                .email(email)
                .phone(phone)
                .password(passwordEncoder.encode("bookhubpassword"))
                .roles(roles)
                .build();

        userService.create(user);
    }
}