package fr.spam.backbookhub.features.category.api.mappers;

import fr.spam.backbookhub.features.category.api.dtos.CategoryResponse;
import fr.spam.backbookhub.features.category.models.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryResponse toCategoryResponse(Category category);
}