package fr.spam.backbookhub.features.review.api.dtos;

import java.time.LocalDateTime;

public record ReviewListResponse(
        Long id,
        int rate,
        String comment,
        LocalDateTime dateReview,
        String book_isbn,
        String username
) {
}