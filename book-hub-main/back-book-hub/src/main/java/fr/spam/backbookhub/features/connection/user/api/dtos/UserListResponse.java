package fr.spam.backbookhub.features.connection.user.api.dtos;

import java.util.UUID;

public record UserListResponse(
        UUID id,
        String username,
        String email
) {}
