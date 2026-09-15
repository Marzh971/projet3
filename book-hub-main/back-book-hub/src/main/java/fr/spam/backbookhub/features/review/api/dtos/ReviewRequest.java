package fr.spam.backbookhub.features.review.api.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReviewRequest(

        @NotBlank(message = "L'ISBN du livre est requis")
        String bookIsbn,

        @NotNull(message = "La note est requise")
        @Min(value = 1, message = "La note doit être comprise entre 1 et 5")
        @Max(value = 5, message = "La note doit être comprise entre 1 et 5")
        Integer rate,

        @NotBlank(message = "Le commentaire est requis")
        String comment

) {
}