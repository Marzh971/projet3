package fr.spam.backbookhub.features.publisher.api.dtos;

import jakarta.validation.constraints.NotBlank;

public record PublisherRequest(

        @NotBlank(message = "Vous n'avez pas renseigné le nom de l'éditeur")
        String name

) {
}