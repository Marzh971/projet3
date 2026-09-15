package fr.spam.backbookhub.features.notification.repositories;

import fr.spam.backbookhub.features.notification.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationJpaRepository extends JpaRepository<Notification, Long> {

    //Optional<List<Notification>> findByUserId(Long id);
}
