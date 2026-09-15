package fr.spam.backbookhub.features.category.services;

import fr.spam.backbookhub.features.category.models.Category;
import fr.spam.backbookhub.features.category.repositories.ICategoryRepository;
import fr.spam.backbookhub.shared.models.ServiceResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService implements ICategoryService {

    private final ICategoryRepository categoryRepository;

    public CategoryService(ICategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ServiceResponse<Category> findById(Long id) {

        try {
            return categoryRepository.findById(id)
                    .map(category -> new ServiceResponse<>(
                            "200",
                            "La catégorie a été trouvée",
                            category
                    ))
                    .orElseGet(() -> new ServiceResponse<>(
                            "404",
                            "La catégorie n'a pas été trouvée",
                            null
                    ));

        } catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Une erreur est survenue lors de la récupération de la catégorie",
                    null
            );
        }
    }

    @Override
    public ServiceResponse<Category> findByNameCategory(String nameCategory) {

        try {
            return categoryRepository.findByNameCategory(nameCategory)
                    .map(category -> new ServiceResponse<>(
                            "200",
                            "La catégorie a été trouvée",
                            category
                    ))
                    .orElseGet(() -> new ServiceResponse<>(
                            "404",
                            "La catégorie n'a pas été trouvée",
                            null
                    ));

        } catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Une erreur est survenue lors de la récupération de la catégorie par son nom",
                    null
            );
        }
    }

    @Override
    public ServiceResponse<List<Category>> findAll() {

        try {
            List<Category> categories = categoryRepository.findAll();

            return new ServiceResponse<>(
                    "200",
                    "La liste des catégories a été trouvée",
                    categories
            );

        } catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Une erreur est survenue lors de la récupération des catégories",
                    null
            );
        }
    }

    @Override
    public ServiceResponse<Category> create(Category category) {

        try {

            if (categoryRepository
                    .findByNameCategory(category.getNameCategory())
                    .isPresent()) {

                return new ServiceResponse<>(
                        "409",
                        "Le nom de la catégorie est déjà utilisé",
                        null
                );
            }

            Category savedCategory = categoryRepository.save(category);

            return new ServiceResponse<>(
                    "201",
                    "La catégorie a été créée",
                    savedCategory
            );

        } catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Une erreur est survenue lors de la création de la catégorie",
                    null
            );
        }
    }

    @Override
    public ServiceResponse<Category> update(Category category) {

        try {

            if (category.getId() == null) {
                return new ServiceResponse<>(
                        "400",
                        "L'identifiant de la catégorie est obligatoire",
                        null
                );
            }

            if (categoryRepository.findById(category.getId()).isEmpty()) {
                return new ServiceResponse<>(
                        "404",
                        "La catégorie n'a pas été trouvée",
                        null
                );
            }

            if (categoryRepository
                    .findByNameCategory(category.getNameCategory())
                    .filter(existingCategory ->
                            !existingCategory.getId().equals(category.getId()))
                    .isPresent()) {

                return new ServiceResponse<>(
                        "409",
                        "Le nom de la catégorie est déjà utilisé",
                        null
                );
            }

            Category updatedCategory = categoryRepository.save(category);

            return new ServiceResponse<>(
                    "200",
                    "La catégorie a été mise à jour",
                    updatedCategory
            );

        } catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Une erreur est survenue lors de la mise à jour de la catégorie",
                    null
            );
        }
    }

    @Override
    public ServiceResponse<Void> delete(Long id) {

        try {

            if (id == null) {
                return new ServiceResponse<>(
                        "400",
                        "L'identifiant de la catégorie est obligatoire",
                        null
                );
            }

            if (categoryRepository.findById(id).isEmpty()) {
                return new ServiceResponse<>(
                        "404",
                        "La catégorie n'a pas été trouvée",
                        null
                );
            }

            categoryRepository.deleteById(id);

            return new ServiceResponse<>(
                    "200",
                    "La catégorie a été supprimée",
                    null
            );

        } catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Une erreur est survenue lors de la suppression de la catégorie",
                    null
            );
        }
    }
}