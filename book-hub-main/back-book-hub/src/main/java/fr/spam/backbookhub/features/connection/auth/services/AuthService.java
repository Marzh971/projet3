package fr.spam.backbookhub.features.connection.auth.services;

import fr.spam.backbookhub.features.connection.auth.api.dtos.AuthLogin;
import fr.spam.backbookhub.features.connection.role.models.Role;
import fr.spam.backbookhub.features.connection.role.services.RoleService;
import fr.spam.backbookhub.features.connection.user.models.User;
import fr.spam.backbookhub.features.connection.user.services.UserService;
import fr.spam.backbookhub.shared.models.ServiceResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements IAuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final RoleService roleService;

    public AuthService(PasswordEncoder passwordEncoder, UserService userService, RoleService roleService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    public ServiceResponse<Void> register(User user) {

        Role roleUser = roleService.findByLabel("USER").getData();
        user.addRole(roleUser);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        ServiceResponse<User> response = userService.create(user);
        System.out.println(response.getMessage());
        if (response.getData() != null){
            return new ServiceResponse<>(
                    "204",
                    "Incription réussie",
                    null
            );
        } else {
            return new ServiceResponse<>(
                    "404",
                    response.getMessage(),
                    null
            );
        }

    }

    @Override
    public ServiceResponse<Void> login(AuthLogin authLogin) {
        ServiceResponse<User> response = userService.findByEmail(authLogin.email());
        if (response.getData() != null){
            if (passwordEncoder.matches(authLogin.password(), response.getData().getPassword())) {
                return new ServiceResponse<>(
                        "200",
                        "Connexion réussi",
                        null
                );
            }
        }
        return new ServiceResponse<>(
                "404",
                "Identifiants incorrect",
                null
        );

    }

    @Override
    public ServiceResponse<Void> logout() {
        return new ServiceResponse<>(
                "204",
                "Déconnexion réussi",
                null
        );
    }
}
