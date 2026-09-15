package fr.spam.backbookhub.features.loan.api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record LoanRequest(

        @NotBlank(message = "Vous n'avez pas renseigné l'ISBN du livre")
        String bookIsbn,

        @NotBlank(message = "Vous n'avez pas renseigné l'utilisateur")
        String userId,

        @NotNull(message = "Vous n'avez pas renseigné la date d'emprunt")
        LocalDateTime dateRetrieved,

        LocalDateTime dateReturned

) {
}