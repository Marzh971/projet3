package fr.spam.backbookhub.features.connection.role.repositories;

import fr.spam.backbookhub.features.connection.role.models.Role;

import java.util.List;
import java.util.Optional;

public interface IRoleRepository {
    Role create(Role role);
    Optional<Role> findById(Long id);
    Optional<Role> findByLabel(String label);
    List<Role> findAll();
}
