package fr.spam.backbookhub.features.connection.role.repositories;

import fr.spam.backbookhub.features.connection.role.models.Role;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RoleRepositoriyJpaAdaptater implements IRoleRepository{

    private final RoleJpaRepository roleJpaRepository;

    public RoleRepositoriyJpaAdaptater(RoleJpaRepository roleJpaRepository) {
        this.roleJpaRepository = roleJpaRepository;
    }

    @Override
    public Role create(Role role) {
        return roleJpaRepository.save(role);
    }

    @Override
    public Optional<Role> findById(Long id) {
        return roleJpaRepository.findById(id);
    }

    @Override
    public Optional<Role> findByLabel(String label) {
        return roleJpaRepository.findByLabel(label);
    }

    @Override
    public List<Role> findAll() {
        return roleJpaRepository.findAll();
    }
}
