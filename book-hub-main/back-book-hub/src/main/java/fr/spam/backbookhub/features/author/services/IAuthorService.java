package fr.spam.backbookhub.features.author.services;

import fr.spam.backbookhub.features.author.models.Author;
import fr.spam.backbookhub.shared.models.ServiceResponse;

import java.util.List;

public interface IAuthorService {

    ServiceResponse<Author> findById(Long id);

    ServiceResponse<Author> findByFirstName(String firstName);

    ServiceResponse<Author> findByLastName(String lastName);

    ServiceResponse<List<Author>> findAll();

    ServiceResponse<Author> create(Author author);

    ServiceResponse<Author> update(Long id, Author author);

    ServiceResponse<Void> delete(Long id);
}