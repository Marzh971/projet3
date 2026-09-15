package fr.spam.backbookhub.features.connection.user.repositories;

import fr.spam.backbookhub.features.connection.user.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<User, UUID> {
    // CRUD operations
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String pseudo);
    Optional<User> findByPhone(String phone);
    boolean existsByUsername(String username);
}