package fr.spam.backbookhub.features.publisher.repositories;

import fr.spam.backbookhub.features.publisher.models.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PublisherJpaRepository extends JpaRepository<Publisher, Long> {

    Optional<Publisher> findByName(String name);

    Optional<Publisher> findById(Long id);
}