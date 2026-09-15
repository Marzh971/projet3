package fr.spam.backbookhub.features.category.repositories;

import fr.spam.backbookhub.features.category.models.Category;

import java.util.List;
import java.util.Optional;

public interface ICategoryRepository {

    Optional<Category> findById(Long id);

    Optional<Category> findByNameCategory(String nameCategory);

    List<Category> findAll();

    Category save(Category category);

    void deleteById(Long id);
}