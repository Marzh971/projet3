package fr.spam.backbookhub.features.category.api.controllers;

import fr.spam.backbookhub.features.category.api.dtos.CategoryResponse;
import fr.spam.backbookhub.features.category.api.mappers.CategoryMapper;
import fr.spam.backbookhub.features.category.models.Category;
import fr.spam.backbookhub.features.category.services.ICategoryService;
import fr.spam.backbookhub.shared.models.ServiceResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final ICategoryService categoryService;
    private final CategoryMapper mapper;


    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    @GetMapping
    public ResponseEntity<ServiceResponse<List<CategoryResponse>>> findAll() {

        ServiceResponse<List<CategoryResponse>> response =
                categoryService.findAll()
                        .map(categories ->
                                categories.stream()
                                        .map(mapper::toCategoryResponse)
                                        .toList()
                        );

        return ResponseEntity
                .status(Integer.parseInt(response.getCode()))
                .body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public ResponseEntity<ServiceResponse<CategoryResponse>> findById(
            @PathVariable Long id) {

        ServiceResponse<CategoryResponse> response =
                categoryService.findById(id)
                        .map(mapper::toCategoryResponse);

        return ResponseEntity
                .status(Integer.parseInt(response.getCode()))
                .body(response);
    }

    @GetMapping("/name/{nameCategory}")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public ResponseEntity<ServiceResponse<CategoryResponse>> findByNameCategory(
            @PathVariable String nameCategory) {

        ServiceResponse<CategoryResponse> response =
                categoryService.findByNameCategory(nameCategory)
                        .map(mapper::toCategoryResponse);

        return ResponseEntity
                .status(Integer.parseInt(response.getCode()))
                .body(response);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public ResponseEntity<ServiceResponse<CategoryResponse>> create(
            @Valid @RequestBody Category categoryCreate) {

        ServiceResponse<CategoryResponse> response =
                categoryService.create(categoryCreate)
                        .map(mapper::toCategoryResponse);

        return ResponseEntity
                .status(Integer.parseInt(response.getCode()))
                .body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public ResponseEntity<ServiceResponse<CategoryResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody Category category) {

        category.setId(id);

        ServiceResponse<CategoryResponse> response =
                categoryService.update(category)
                        .map(mapper::toCategoryResponse);

        return ResponseEntity
                .status(Integer.parseInt(response.getCode()))
                .body(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public ResponseEntity<ServiceResponse<Void>> delete(
            @PathVariable Long id) {

        ServiceResponse<Void> response =
                categoryService.delete(id);

        return ResponseEntity
                .status(Integer.parseInt(response.getCode()))
                .body(response);
    }
}