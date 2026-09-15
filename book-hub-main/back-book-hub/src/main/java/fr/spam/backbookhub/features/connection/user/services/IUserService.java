package fr.spam.backbookhub.features.connection.user.services;

import fr.spam.backbookhub.features.connection.user.models.User;
import fr.spam.backbookhub.shared.models.ServiceResponse;

import java.util.List;
import java.util.UUID;

public interface IUserService {

    /**
     * Retrouve un utilisateur en le cherchant par son id.
     *
     * @param id l'identifiant unique de l'utilisateur recherché.
     * @return un ServiceResponse contenant un objet User. La réponse inclue des informations
     * tels que le code de statut, le message et le chargement utile de données comprenant l'utilisateur.
     */
    ServiceResponse<User> findById(UUID id);

    /**
     * Retrouve un utilisateur en le cherchant par son pseudo.
     *
     * @param pseudo le pseudo de l'utilisateur recherché.
     * @return un ServiceResponse contenant un objet User. La réponse inclue des informations
     * tels que le code de statut, le message et le chargement utile de données comprenant l'utilisateur.
     */
    ServiceResponse<User> findByPseudo(String pseudo);

    /**
     * Retrouve un utilisateur en le cherchant par son email.
     *
     * @param email l'email de l'utilisateur recherché.
     * @return un ServiceResponse contenant un objet User. La réponse inclue des informations
     * tels que le code de statut, le message et le chargement utile de données comprenant l'utilisateur.
     */
    ServiceResponse<User> findByEmail(String email);

    /**
     * Retrouve une liste contenant tous les utilisateurs.
     *
     * @return un ServiceResponse contenant une liste d'objets User. La réponse inclue des informations
     * tels que le code de statut, le message et le chargement utile de données comprenant tous les utilisateurs.
     */
    ServiceResponse<List<User>> findAll();

    /**
     * Crée un nouvel utilisateur.
     *
     * @param user l'utilisateur à créer.
     * @return un ServiceResponse contenant l'objet User créé. La réponse inclue des informations
     * tels que le code de statut, le message et le chargement utile de données comprenant l'utilisateur.
     */
    ServiceResponse<User> create(User user);

    /**
     * Met à jour un utilisateur existant.
     *
     * @param user l'utilisateur à mettre à jour, identifié par son id.
     * @return un ServiceResponse contenant l'objet User mis à jour. La réponse inclue des informations
     * tels que le code de statut, le message et le chargement utile de données comprenant l'utilisateur.
     */
    ServiceResponse<User> update(User user);

    /**
     * Supprime un utilisateur en le cherchant par son id.
     *
     * @param id l'identifiant unique de l'utilisateur à supprimer.
     * @return un ServiceResponse sans chargement utile de données. La réponse inclue des informations
     * tels que le code de statut et le message.
     */
    ServiceResponse<Void> delete(UUID id);
}
