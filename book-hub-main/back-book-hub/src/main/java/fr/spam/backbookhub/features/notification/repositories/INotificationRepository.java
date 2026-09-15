package fr.spam.backbookhub.features.notification.repositories;

import fr.spam.backbookhub.features.notification.models.Notification;

import java.util.List;
import java.util.Optional;

public interface INotificationRepository {

    // CRUD operations
    //Optional<List<Notification>> findByUserId(Long id);
    Notification save(Notification notification);
}
