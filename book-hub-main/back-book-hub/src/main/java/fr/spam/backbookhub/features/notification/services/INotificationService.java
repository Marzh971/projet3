package fr.spam.backbookhub.features.notification.services;

import fr.spam.backbookhub.features.notification.models.Notification;
import fr.spam.backbookhub.shared.models.ServiceResponse;

import java.util.List;

public interface INotificationService {

    /**
     * Recherche les notifications d'un utilisateur à partir de son identifiant.
     *
     * @param id l'identifiant de l'utilisateur
     * @return une réponse de service contenant la notification associée à l'utilisateur
     */
    //ServiceResponse<List<Notification>> findByUserId(Long id);

    /**
     * Enregistre une notification.
     *
     * @param notification la notification à sauvegarder
     * @return une réponse de service contenant la notification sauvegardée
     */
    ServiceResponse<Notification> create(Notification notification);
}
