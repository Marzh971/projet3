package fr.spam.backbookhub.features.connection.role.repositories;

import fr.spam.backbookhub.features.connection.role.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleJpaRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByLabel(String label);
}
