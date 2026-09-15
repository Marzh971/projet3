package fr.spam.backbookhub.features.author.repositories;

import fr.spam.backbookhub.features.author.models.Author;

import java.util.List;
import java.util.Optional;

public interface IAuthorRepository {

    Optional<Author> findById(Long id);

    Optional<Author> findByFirstName(String firstName);

    Optional<Author> findByLastName(String lastName);

    List<Author> findAll();

    Author save(Author author);

    void deleteById(Long id);
}