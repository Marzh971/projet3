package fr.spam.backbookhub.features.author.api.mappers;

import fr.spam.backbookhub.features.author.api.dtos.AuthorListResponse;
import fr.spam.backbookhub.features.author.api.dtos.AuthorRequest;
import fr.spam.backbookhub.features.author.api.dtos.AuthorResponse;
import fr.spam.backbookhub.features.author.models.Author;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    Author toAuthor(AuthorRequest authorCreateRequest);
    AuthorResponse toAuthorResponse(Author author);
    AuthorListResponse toAuthorListResponse(Author author);

}