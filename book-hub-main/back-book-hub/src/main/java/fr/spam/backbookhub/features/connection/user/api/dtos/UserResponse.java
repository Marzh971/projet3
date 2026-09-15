package fr.spam.backbookhub.features.connection.user.api.dtos;

import jakarta.persistence.Column;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String username,
        String email,
        String firstName,
        String lastName,
        String phone
) {}
