package fr.spam.backbookhub.features.connection.user.repositories;

import fr.spam.backbookhub.features.connection.user.models.User;
import fr.spam.backbookhub.shared.models.ServiceResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IUserRepository {

    // CRUD operations
    Optional<User> findById(UUID id);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findByPhone(String phone);
    List<User> findAll();
    User save(User user);
    void deleteById(UUID id);
    boolean existsByUsername(String username);

}
