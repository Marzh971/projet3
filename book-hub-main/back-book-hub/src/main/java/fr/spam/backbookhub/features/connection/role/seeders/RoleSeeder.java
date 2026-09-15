package fr.spam.backbookhub.features.connection.role.seeders;

import fr.spam.backbookhub.features.connection.role.models.Role;
import fr.spam.backbookhub.features.connection.role.services.IRoleService;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class RoleSeeder {

    private final IRoleService roleService;

    public RoleSeeder(IRoleService roleService) {
        this.roleService = roleService;
    }

    public void seed() {
        seedRoleIfAbsent("USER");
        seedRoleIfAbsent("LIBRARIAN");
        seedRoleIfAbsent("ADMIN");
    }

    private void seedRoleIfAbsent(String label) {
        var existing = roleService.findByLabel(label);
        if (existing.getData() != null) {
            return;
        }

        Role role = Role.builder()
                .label(label)
                .build();

        roleService.create(role);
    }
}