package fr.spam.backbookhub.features.category.repositories;

import fr.spam.backbookhub.features.category.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryJpaRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByNameCategory(String nameCategory);
}
