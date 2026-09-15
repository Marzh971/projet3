package fr.spam.backbookhub.features.publisher.repositories;

import fr.spam.backbookhub.features.publisher.models.Publisher;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PublisherRepositoryJpaAdapter implements IPublisherRepository {

    private final PublisherJpaRepository publisherJpaRepository;

    public PublisherRepositoryJpaAdapter(PublisherJpaRepository publisherJpaRepository) {
        this.publisherJpaRepository = publisherJpaRepository;
    }

    @Override
    public Optional<Publisher> findById(Long id) {
        return publisherJpaRepository.findById(id);
    }

    @Override
    public Optional<Publisher> findByName(String name) {
        return publisherJpaRepository.findByName(name);
    }

    @Override
    public List<Publisher> findAll() {
        return publisherJpaRepository.findAll();
    }

    @Override
    public Publisher create(Publisher publisher) {
        return publisherJpaRepository.save(publisher);
    }

    @Override
    public Publisher update(Publisher publisher) {
        return publisherJpaRepository.save(publisher);
    }

    @Override
    public void delete(Publisher publisher) {
        publisherJpaRepository.delete(publisher);
    }
}