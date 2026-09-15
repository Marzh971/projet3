package fr.spam.backbookhub.features.connection.role.services;

import fr.spam.backbookhub.features.connection.role.models.Role;
import fr.spam.backbookhub.shared.models.ServiceResponse;

import java.util.List;

public interface IRoleService {
    ServiceResponse<Role> create(Role role);

    ServiceResponse<Role> findById(Long id);

    ServiceResponse<Role> findByLabel(String label);

    ServiceResponse<List<Role>> findAll();
}
