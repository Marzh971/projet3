package fr.spam.backbookhub.features.copy.api.dtos;

import java.time.LocalDateTime;
import java.util.List;

public record CopyResponse(
        Long id,
        String barcode,
        String bookIsbn,
        String bookTitle,
        String bookAuthor,
        String bookPublisher,
        List<String> bookCategories,
        LocalDateTime purchaseDate
) {}