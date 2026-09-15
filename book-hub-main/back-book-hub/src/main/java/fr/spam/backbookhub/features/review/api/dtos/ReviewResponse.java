package fr.spam.backbookhub.features.review.api.dtos;

import java.time.LocalDateTime;

public record ReviewResponse(
        Long id,
        int rate,
        String comment,
        LocalDateTime dateReview,
        String bookIsbn,
        String username
) {
}