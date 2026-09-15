package fr.spam.backbookhub.features.connection.auth.api.dtos;

import java.util.List;
import java.util.UUID;

public record CurrentUserDto (
        UUID id,
        String username,
        List<String> roles)
{
}