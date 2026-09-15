package fr.spam.backbookhub.features.copy.api.mappers;

import fr.spam.backbookhub.features.author.models.Author;
import fr.spam.backbookhub.features.book.models.Book;
import fr.spam.backbookhub.features.copy.api.dtos.CopyListResponse;
import fr.spam.backbookhub.features.copy.api.dtos.CopyRequest;
import fr.spam.backbookhub.features.copy.api.dtos.CopyResponse;
import fr.spam.backbookhub.features.copy.models.Copy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CopyMapper {

    @Mapping(target = "book", source = "copyRequest.bookIsbn", qualifiedByName = "isbnToBook")
    Copy toCopy(CopyRequest copyRequest);

    @Named("isbnToBook")
    default Book isbnToBook(String isbn) {
        if (isbn == null) return null;
        return Book.builder().isbn(isbn).build();
    }

    @Mapping(target = "bookIsbn", source = "book.isbn")
    @Mapping(target = "bookTitle", source = "book.title")
    @Mapping(target = "bookAuthor", source = "book.authors", qualifiedByName = "authorsToString")
    @Mapping(target = "bookPublisher", source = "book.publisher.name")
    @Mapping(target = "bookCategories", source = "book.categories", qualifiedByName = "categoriesToStringList")
    CopyResponse toCopyResponse(Copy copy);
    @Mapping(target = "bookTitle", source = "book.title")
    CopyListResponse toCopyListResponse(Copy copy);

    @Named("authorsToString")
    default String authorsToString(java.util.Set<Author> authors) {
        if (authors == null || authors.isEmpty()) {
            return "Inconnu";
        }
        return authors.stream()
                .map(author -> author.getFirstName() + " " + author.getLastName())
                .collect(Collectors.joining(", "));
    }

    @Named("categoriesToStringList")
    default List<String> categoriesToStringList(java.util.Set<fr.spam.backbookhub.features.category.models.Category> categories) {
        if (categories == null) {
            return List.of();
        }
        return categories.stream()
                .map(fr.spam.backbookhub.features.category.models.Category::getNameCategory)
                .toList();
    }
}