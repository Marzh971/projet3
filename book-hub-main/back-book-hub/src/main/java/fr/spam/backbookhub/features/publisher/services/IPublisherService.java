package fr.spam.backbookhub.features.publisher.services;

import fr.spam.backbookhub.features.publisher.models.Publisher;
import fr.spam.backbookhub.shared.models.ServiceResponse;

import java.util.List;

public interface IPublisherService {

    ServiceResponse<Publisher> findById(Long id);

    ServiceResponse<Publisher> findByName(String name);

    ServiceResponse<List<Publisher>> findAll();

    ServiceResponse<Publisher> create(Publisher publisher);

    ServiceResponse<Publisher> delete(Publisher publisher);

    ServiceResponse<Publisher> update(Long id, Publisher publisher);

    ServiceResponse<Void> delete(Long id);
}