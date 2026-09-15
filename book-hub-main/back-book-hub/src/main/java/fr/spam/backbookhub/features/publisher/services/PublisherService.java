package fr.spam.backbookhub.features.publisher.services;

import fr.spam.backbookhub.features.publisher.models.Publisher;
import fr.spam.backbookhub.features.publisher.repositories.IPublisherRepository;
import fr.spam.backbookhub.shared.models.ServiceResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublisherService implements IPublisherService {

    private final IPublisherRepository publisherRepository;

    public PublisherService(IPublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    @Override
    public ServiceResponse<Publisher> findById(Long id) {
        try {
            return new ServiceResponse<>(
                    "200",
                    "L'éditeur a été trouvé",
                    publisherRepository.findById(id).orElse(null)
            );
        } catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Une erreur est survenue lors de la récupération de l'éditeur",
                    null
            );
        }
    }

    @Override
    public ServiceResponse<Publisher> findByName(String name) {
        try {
            return new ServiceResponse<>(
                    "200",
                    "L'éditeur a été trouvé",
                    publisherRepository.findByName(name).orElse(null)
            );
        } catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Une erreur est survenue lors de la récupération de l'éditeur",
                    null
            );
        }
    }

    @Override
    public ServiceResponse<List<Publisher>> findAll() {
        try {
            return new ServiceResponse<>(
                    "200",
                    "La liste des éditeurs a été trouvée",
                    publisherRepository.findAll()
            );
        } catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Une erreur est survenue lors de la récupération de la liste des éditeurs",
                    null
            );
        }
    }

    @Override
    public ServiceResponse<Publisher> create(Publisher publisher) {

        if (publisherRepository.findByName(publisher.getName()).isPresent()) {
            return new ServiceResponse<>(
                    "400",
                    "Cet éditeur existe déjà",
                    null
            );
        }

        try {
            Publisher savedPublisher = publisherRepository.create(publisher);

            return new ServiceResponse<>(
                    "200",
                    "L'éditeur a été créé",
                    savedPublisher
            );
        } catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Une erreur est survenue lors de la création de l'éditeur",
                    null
            );
        }
    }

    @Override
    public ServiceResponse<Publisher> delete(Publisher publisher) {
        return null;
    }

    @Override
    public ServiceResponse<Publisher> update(Long id, Publisher publisher) {
        try {

            if (publisherRepository.findById(id).isEmpty()) {
                return new ServiceResponse<>(
                        "404",
                        "L'éditeur n'a pas été trouvé",
                        null
                );
            }

            publisher.setId(id);
            Publisher updatedPublisher = publisherRepository.update(publisher);

            return new ServiceResponse<>(
                    "200",
                    "L'éditeur a été mis à jour",
                    updatedPublisher
            );

        } catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Une erreur est survenue lors de la mise à jour de l'éditeur",
                    null
            );
        }
    }

    @Override
    public ServiceResponse<Void> delete(Long id) {

        try {

            var publisher = publisherRepository.findById(id);

            if (publisher.isEmpty()) {
                return new ServiceResponse<>(
                        "404",
                        "L'éditeur n'a pas été trouvé",
                        null
                );
            }

            publisherRepository.delete(publisher.get());

            return new ServiceResponse<>(
                    "200",
                    "L'éditeur a été supprimé",
                    null
            );

        } catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Une erreur est survenue lors de la suppression de l'éditeur",
                    null
            );
        }
    }
}