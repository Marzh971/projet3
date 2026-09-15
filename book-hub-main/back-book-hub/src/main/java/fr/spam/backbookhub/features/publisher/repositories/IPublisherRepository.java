package fr.spam.backbookhub.features.publisher.repositories;

import fr.spam.backbookhub.features.publisher.models.Publisher;

import java.util.List;
import java.util.Optional;

public interface IPublisherRepository {

    Optional<Publisher> findById(Long id);

    Optional<Publisher> findByName(String name);

    List<Publisher> findAll();

    Publisher create(Publisher publisher);

    Publisher update(Publisher publisher);

    void delete(Publisher publisher);

}