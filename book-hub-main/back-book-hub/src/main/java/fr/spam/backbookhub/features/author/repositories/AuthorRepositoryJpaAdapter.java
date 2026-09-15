package fr.spam.backbookhub.features.author.repositories;

import fr.spam.backbookhub.features.author.models.Author;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AuthorRepositoryJpaAdapter implements IAuthorRepository {

    private final AuthorJpaRepository authorJpaRepository;

    public AuthorRepositoryJpaAdapter(AuthorJpaRepository authorJpaRepository) {
        this.authorJpaRepository = authorJpaRepository;
    }

    @Override
    public Optional<Author> findById(Long id) {
        return authorJpaRepository.findById(id);
    }

    @Override
    public Optional<Author> findByFirstName(String firstName) {
        return authorJpaRepository.findByFirstName(firstName);
    }

    @Override
    public Optional<Author> findByLastName(String lastName) {
        return authorJpaRepository.findByLastName(lastName);
    }

    @Override
    public List<Author> findAll() {
        return authorJpaRepository.findAll();
    }

    @Override
    public Author save(Author author) {
        return authorJpaRepository.save(author);
    }

    @Override
    public void deleteById(Long id) {
        authorJpaRepository.deleteById(id);
    }
}