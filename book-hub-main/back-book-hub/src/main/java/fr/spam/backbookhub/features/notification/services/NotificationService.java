package fr.spam.backbookhub.features.notification.services;

import fr.spam.backbookhub.features.notification.models.Notification;
import fr.spam.backbookhub.features.notification.repositories.INotificationRepository;
import fr.spam.backbookhub.shared.models.ServiceResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService implements INotificationService {

    private final INotificationRepository notificationRepository;

    public NotificationService(INotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

//    @Override
//    public ServiceResponse<List<Notification>> findByUserId(Long id) {
//        try {
//            return new ServiceResponse<>(
//                    "200",
//                    "L'utilisateur a été trouvé",
//                    notificationRepository.findByUserId(id).orElse(null)
//            );
//        } catch (Exception e) {
//            return new ServiceResponse<>(
//                    "500",
//                    "Une erreur est survenue",
//                    null
//            );
//        }
//    }


    @Override
    public ServiceResponse<Notification> create(Notification notification) {
        try {
            return new ServiceResponse<>(
                    "200",
                    "L'utilisateur a été trouvé",
                    notificationRepository.save(notification)
            );
        } catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Une erreur est survenue",
                    null
            );
        }
    }
}
