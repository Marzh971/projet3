package fr.spam.backbookhub.features.copy.api.dtos;

import java.time.LocalDateTime;

public record CopyListResponse(
        Long id,
        String barcode,
        String bookTitle,
        LocalDateTime purchaseDate
) {}
