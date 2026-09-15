package fr.spam.backbookhub.features.connection.user.api.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record UserRequest(
        @NotBlank(message = "Vous n'avez pas renseigné de pseudo")
        @Size(min = 3, max= 20, message = "Le pseudo doit être compris entre 3 et 20 caractères.")
        String username,

        @NotBlank(message = "Vous n'avez pas renseigné d'email")
        @Email(message = "Le format du mail n'est pas valide.")
        String email,

        @NotBlank(message = "Vous devez renseigné votre prénom.")
        @Size(min = 3, max= 30, message = "Le prénom de famille doit être entre 3 et 30 caratères.")
        String firstName,

        @NotBlank(message = "Vous devez renseigné votre nom de famille.")
        @Size(min = 2, max= 30, message = "Le nom de famille doit être entre 2 et 30 caratères.")
        String lastName,

        @NotBlank(message = "Vous n'avez pas renseigné de numéro de téléphone.")
        @Size(min = 10, max= 10, message = "Le format du numéro de téléphone n'est pas valide.")
        String phone,

        @NotBlank(message = "Vous devez renseigner votre mot de passe.")
        @Size(min = 8, max= 30, message = "Le mot de passe doit être entre 8 et 30 caractères.")
        String password
) {
}