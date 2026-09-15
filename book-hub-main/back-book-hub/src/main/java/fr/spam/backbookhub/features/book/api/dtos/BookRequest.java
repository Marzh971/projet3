package fr.spam.backbookhub.features.book.api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;

public record BookRequest(
        @NotBlank(message = "Vous n'avez pas renseigné d'ISBN")
        String isbn,
        @NotBlank(message = "Vous n'avez pas renseigné de titre")
        String title,
        String description,
        String cover,
        @NotNull(message = "Vous n'avez pas renseigné de date")
        LocalDate publicationDate,
        @Positive(message = "Le nombre de pages doit être supérieur à 0")
        int pageNumber,
        Integer copyNumber,
        Long publisherId,
        List<Long> categoryIds,
        List<Long> authorIds
) {
}
