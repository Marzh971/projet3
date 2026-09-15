package fr.spam.backbookhub.features.author.services;



import fr.spam.backbookhub.features.author.models.Author;
import fr.spam.backbookhub.features.author.repositories.IAuthorRepository;
import fr.spam.backbookhub.shared.models.ServiceResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService implements IAuthorService {

    private final IAuthorRepository authorRepository;

    public AuthorService(IAuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public ServiceResponse<Author> findById(Long id) {
        try {
            return new ServiceResponse<>(
                    "200",
                    "L'auteur a été trouvé",
                    authorRepository.findById(id).orElse(null)
            );
        } catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Erreur lors de la récupération de l'auteur",
                    null
            );
        }
    }

    @Override
    public ServiceResponse<Author> findByFirstName(String firstName) {
        try {
            return new ServiceResponse<>(
                    "200",
                    "L'auteur a été trouvé",
                    authorRepository.findByFirstName(firstName).orElse(null)
            );
        } catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Erreur lors de la récupération de l'auteur",
                    null
            );
        }
    }

    @Override
    public ServiceResponse<Author> findByLastName(String lastName) {
        try {
            return new ServiceResponse<>(
                    "200",
                    "L'auteur a été trouvé",
                    authorRepository.findByLastName(lastName).orElse(null)
            );
        } catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Erreur lors de la récupération de l'auteur",
                    null
            );
        }
    }

    @Override
    public ServiceResponse<List<Author>> findAll() {
        try {
            return new ServiceResponse<>(
                    "200",
                    "Liste des auteurs récupérée",
                    authorRepository.findAll()
            );
        } catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Erreur lors de la récupération des auteurs",
                    null
            );
        }
    }

    @Override
    public ServiceResponse<Author> create(Author author) {
        try {
            Author savedAuthor = authorRepository.save(author);

            return new ServiceResponse<>(
                    "200",
                    "Auteur créé",
                    savedAuthor
            );
        } catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Erreur lors de la création",
                    null
            );
        }
    }

    @Override
    public ServiceResponse<Author> update(Long id, Author author) {

        try {

            if (authorRepository.findById(id).isEmpty()) {
                return new ServiceResponse<>(
                        "404",
                        "Auteur introuvable",
                        null
                );
            }

            author.setId(id);
            Author updatedAuthor = authorRepository.save(author);

            return new ServiceResponse<>(
                    "200",
                    "Auteur mis à jour",
                    updatedAuthor
            );

        } catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Erreur lors de la mise à jour",
                    null
            );
        }
    }

    @Override
    public ServiceResponse<Void> delete(Long id) {

        try {

            authorRepository.deleteById(id);

            return new ServiceResponse<>(
                    "200",
                    "Auteur supprimé",
                    null
            );

        } catch (Exception e) {

            return new ServiceResponse<>(
                    "500",
                    "Erreur lors de la suppression",
                    null
            );
        }
    }
}