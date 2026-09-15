package fr.spam.backbookhub.features.notification.api.mappers;

import fr.spam.backbookhub.features.notification.api.dtos.NotificationResponse;
import fr.spam.backbookhub.features.notification.models.Notification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    NotificationResponse toNotificationResponse(Notification notification);
}
