package fr.spam.backbookhub.features.author.repositories;

import fr.spam.backbookhub.features.author.models.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorJpaRepository extends JpaRepository<Author, Long> {

    Optional<Author> findByFirstName(String firstName);

    Optional<Author> findByLastName(String lastName);
}