package fr.spam.backbookhub.features.author.api.dtos;

import jakarta.validation.constraints.NotBlank;

public record AuthorRequest(

        @NotBlank(message = "Vous n'avez pas renseigné le prénom")
        String firstName,

        @NotBlank(message = "Vous n'avez pas renseigné le nom")
        String lastName

) {
}