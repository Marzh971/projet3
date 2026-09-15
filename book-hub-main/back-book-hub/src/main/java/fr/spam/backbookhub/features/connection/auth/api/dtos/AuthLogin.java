package fr.spam.backbookhub.features.connection.auth.api.dtos;

public record AuthLogin(
        String email,
        String password
) {
}
