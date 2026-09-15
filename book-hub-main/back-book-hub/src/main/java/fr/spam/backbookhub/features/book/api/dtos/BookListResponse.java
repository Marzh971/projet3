package fr.spam.backbookhub.features.book.api.dtos;

import fr.spam.backbookhub.features.author.api.dtos.AuthorListResponse;
import fr.spam.backbookhub.features.category.api.dtos.CategoryResponse;

import java.util.List;


public record BookListResponse(
        String isbn,
        String title,
        String cover,
        List<AuthorListResponse> authors,
        List<CategoryResponse> categories

) {

}
