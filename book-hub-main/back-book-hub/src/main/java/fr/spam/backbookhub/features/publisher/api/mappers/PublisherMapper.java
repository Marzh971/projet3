package fr.spam.backbookhub.features.publisher.api.mappers;

import fr.spam.backbookhub.features.publisher.api.dtos.PublisherListResponse;
import fr.spam.backbookhub.features.publisher.api.dtos.PublisherRequest;
import fr.spam.backbookhub.features.publisher.api.dtos.PublisherResponse;
import fr.spam.backbookhub.features.publisher.models.Publisher;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PublisherMapper {

    Publisher toPublisher(PublisherRequest publisherRequest);

    PublisherResponse toPublisherResponse(Publisher publisher);

    PublisherListResponse toPublisherListResponse(Publisher publisher);

}