package fr.spam.backbookhub.features.category.repositories;

import fr.spam.backbookhub.features.category.models.Category;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CategoryRepositoryJpaAdapter implements ICategoryRepository {

    private final CategoryJpaRepository categoryJpaRepository;

    public CategoryRepositoryJpaAdapter(CategoryJpaRepository categoryJpaRepository) {
        this.categoryJpaRepository = categoryJpaRepository;
    }

    @Override
    public Optional<Category> findById(Long id) {
        return categoryJpaRepository.findById(id);
    }

    @Override
    public Optional<Category> findByNameCategory(String nameCategory) {
        return categoryJpaRepository.findByNameCategory(nameCategory);
    }

    @Override
    public List<Category> findAll() {
        return categoryJpaRepository.findAll();
    }

    @Override
    public Category save(Category category) {
        return categoryJpaRepository.save(category);
    }

    @Override
    public void deleteById(Long id) {
        categoryJpaRepository.deleteById(id);
    }
}