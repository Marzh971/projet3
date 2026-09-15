package fr.spam.backbookhub.features.copy.api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CopyRequest(
        @NotBlank(message = "Vous n'avez pas renseigné le code-barre")
        @Size(min = 8, max = 8, message = "Le code-barre doit contenir 50 caractères")
        String barcode,

        @NotBlank(message = "Vous n'avez pas renseigné d'ISBN")
        String bookIsbn
) {
}
