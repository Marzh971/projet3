package fr.spam.backbookhub.features.book.api.mappers;

import fr.spam.backbookhub.features.author.models.Author;
import fr.spam.backbookhub.features.book.api.dtos.BookListResponse;
import fr.spam.backbookhub.features.book.api.dtos.BookRequest;
import fr.spam.backbookhub.features.book.api.dtos.BookResponse;
import fr.spam.backbookhub.features.book.models.Book;
import fr.spam.backbookhub.features.category.models.Category;
import fr.spam.backbookhub.features.review.api.mappers.ReviewMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = ReviewMapper.class)
public interface BookMapper {
    @Mapping(target = "publisher.id", source = "publisherId")
    @Mapping(target = "categories", expression = "java(mapIdsToCategories(bookCreateRequest.categoryIds()))")
    @Mapping(target = "authors", expression = "java(mapIdsToAuthors(bookCreateRequest.authorIds()))")
    Book toBook(BookRequest bookCreateRequest);

    @Mapping(target = "categories", source = "categories")
    @Mapping(target = "authors", source = "authors")
    @Mapping(target = "reviews", source = "reviews")
    @Mapping(target = "publisher", source = "publisher")
    BookResponse toBookResponse(Book book);

    @Mapping(target = "categories", source = "categories")
    @Mapping(target = "authors", source = "authors")
    BookListResponse toBookListResponse(Book book);

    default Set<Category> mapIdsToCategories(List<Long> ids) {
        if (ids == null) {
            return new HashSet<>();
        }
        return ids.stream()
                .map(id -> Category.builder().id(id).build())
                .collect(Collectors.toSet());
    }

    default Set<Author> mapIdsToAuthors(List<Long> ids) {
        if (ids == null) {
            return new HashSet<>();
        }
        return ids.stream()
                .map(id -> Author.builder().id(id).build())
                .collect(Collectors.toSet());
    }
}

