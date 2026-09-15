package fr.spam.backbookhub.features.book.api.dtos;

import fr.spam.backbookhub.features.author.api.dtos.AuthorListResponse;
import fr.spam.backbookhub.features.category.api.dtos.CategoryResponse;
import fr.spam.backbookhub.features.publisher.api.dtos.PublisherResponse;
import fr.spam.backbookhub.features.review.api.dtos.ReviewListResponse;

import java.time.LocalDate;
import java.util.List;

public record BookResponse(
        String isbn,
        String title,
        String description,
        String cover,
        LocalDate publicationDate,
        int pageNumber,
        PublisherResponse publisher,
        List<ReviewListResponse> reviews,
        List<AuthorListResponse> authors,
        List<CategoryResponse> categories
) {
}
