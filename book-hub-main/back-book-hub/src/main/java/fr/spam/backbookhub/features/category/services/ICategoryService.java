package fr.spam.backbookhub.features.category.services;

import fr.spam.backbookhub.features.category.models.Category;
import fr.spam.backbookhub.shared.models.ServiceResponse;

import java.util.List;

public interface ICategoryService {

    /**
     * Retrouve une catégorie par son identifiant.
     *
     * @param id identifiant de la catégorie recherchée
     * @return la catégorie trouvée
     */
    ServiceResponse<Category> findById(Long id);

    /**
     * Retrouve une catégorie par son nom.
     *
     * @param nameCategory nom de la catégorie recherchée
     * @return la catégorie trouvée
     */
    ServiceResponse<Category> findByNameCategory(String nameCategory);

    /**
     * Retourne toutes les catégories.
     *
     * @return la liste des catégories
     */
    ServiceResponse<List<Category>> findAll();

    /**
     * Crée une nouvelle catégorie.
     *
     * @param category catégorie à créer
     * @return la catégorie créée
     */
    ServiceResponse<Category> create(Category category);

    /**
     * Met à jour une catégorie.
     *
     * @param category catégorie à modifier
     * @return la catégorie mise à jour
     */
    ServiceResponse<Category> update(Category category);

    /**
     * Supprime une catégorie.
     *
     * @param id identifiant de la catégorie à supprimer
     * @return une réponse sans contenu
     */
    ServiceResponse<Void> delete(Long id);
}