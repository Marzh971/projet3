package fr.spam.backbookhub.features.connection.role.services;

import fr.spam.backbookhub.features.connection.role.models.Role;
import fr.spam.backbookhub.features.connection.role.repositories.IRoleRepository;
import fr.spam.backbookhub.shared.models.ServiceResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService implements IRoleService {

    private final IRoleRepository roleRepository;

    public RoleService(IRoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public ServiceResponse<Role> create(Role role) {
        try {
            return new ServiceResponse<Role>(
                    "200",
                    "Le rôle a bien été créé",
                    roleRepository.create(role)
            );
        } catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Une erreur est survenue lors de la création du rôle",
                    null
            );
        }
    }

    @Override
    public ServiceResponse<Role> findById(Long id) {
        try {
            return new ServiceResponse<Role>(
                    "200",
                    "Le rôle a été trouvé",
                    roleRepository.findById(id).orElseThrow()
            );
        } catch (Exception e) {
            return new ServiceResponse<>(
                    "404",
                    "Le rôle n'a pas été trouvé",
                    null
            );
        }
    }

    @Override
    public ServiceResponse<Role> findByLabel(String label) {
        try {
            return new ServiceResponse<>(
                    "200",
                    "Le rôle " + label + " a bien été trouvé.",
                    roleRepository.findByLabel(label).orElseThrow()
            );
        } catch (Exception e) {
            return new ServiceResponse<>(
                    "404",
                    "Le rôle n'a pas été trouvé.",
                    null
            );
        }
    }

    @Override
    public ServiceResponse<List<Role>> findAll() {
        try {
            return new ServiceResponse<>(
                    "200",
                    "Les rôles ont été trouvés",
                    roleRepository.findAll()
            );
        } catch (Exception e) {
            return new ServiceResponse<>(
                    "404",
                    "Aucun rôle n'a été trouvé",
                    null
            );
        }
    }
}
