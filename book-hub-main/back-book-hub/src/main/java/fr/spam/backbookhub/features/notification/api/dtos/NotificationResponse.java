package fr.spam.backbookhub.features.notification.api.dtos;

import java.time.LocalDateTime;

public record NotificationResponse(
        Long id,
        LocalDateTime dateNotification,
        String text,
        boolean isRead,
        String typeNotification
) {}
