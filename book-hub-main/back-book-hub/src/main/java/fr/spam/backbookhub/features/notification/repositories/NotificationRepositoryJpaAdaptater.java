package fr.spam.backbookhub.features.notification.repositories;

import fr.spam.backbookhub.features.notification.models.Notification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class NotificationRepositoryJpaAdaptater implements INotificationRepository {

    private final NotificationJpaRepository notificationJpaRepository;

    public NotificationRepositoryJpaAdaptater(NotificationJpaRepository notificationJpaRepository) {
        this.notificationJpaRepository = notificationJpaRepository;
    }

//    @Override
//    public Optional<List<Notification>> findByUserId(Long id) {
//        return notificationJpaRepository.findByUserId(id);
//    }

    @Override
    public Notification save(Notification notification) {
        return notificationJpaRepository.save(notification);
    }
}