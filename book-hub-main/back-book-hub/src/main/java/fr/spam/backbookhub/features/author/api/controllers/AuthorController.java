package fr.spam.backbookhub.features.author.api.controllers;

import fr.spam.backbookhub.features.author.api.dtos.AuthorListResponse;
import fr.spam.backbookhub.features.author.api.dtos.AuthorRequest;
import fr.spam.backbookhub.features.author.api.dtos.AuthorResponse;
import fr.spam.backbookhub.features.author.api.mappers.AuthorMapper;
import fr.spam.backbookhub.features.author.services.IAuthorService;
import fr.spam.backbookhub.shared.models.ServiceResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/author")
public class AuthorController {

    private final IAuthorService authorService;
    private final AuthorMapper mapper;

    public AuthorController(IAuthorService authorService, AuthorMapper mapper) {
        this.authorService = authorService;
        this.mapper = mapper;
    }

    @GetMapping("")
    public ResponseEntity<ServiceResponse<List<AuthorListResponse>>> findAll() {
        return ResponseEntity
                .status(200)
                .body(
                        authorService
                                .findAll()
                                .map(authors -> authors
                                        .stream()
                                        .map(mapper::toAuthorListResponse)
                                        .toList())
                );
    }

    @GetMapping("/lastname/{lastName}")
    public ResponseEntity<ServiceResponse<AuthorResponse>> findByLastName(@PathVariable String lastName) {

        ServiceResponse<AuthorResponse> response = authorService
                .findByLastName(lastName)
                .map(mapper::toAuthorResponse);

        return ResponseEntity
                .status(Integer.parseInt(response.getCode()))
                .body(response);
    }

    @GetMapping("/firstname/{firstName}")
    public ResponseEntity<ServiceResponse<AuthorResponse>> findByFirstName(@PathVariable String firstName) {

        ServiceResponse<AuthorResponse> response = authorService
                .findByFirstName(firstName)
                .map(mapper::toAuthorResponse);

        return ResponseEntity
                .status(Integer.parseInt(response.getCode()))
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponse<AuthorResponse>> findById(@PathVariable Long id) {
        ServiceResponse<AuthorResponse> response = authorService
                .findById(id)
                .map(mapper::toAuthorResponse);

        return ResponseEntity
                .status(Integer.parseInt(response.getCode()))
                .body(response);
    }

    @PostMapping("")
    public ResponseEntity<ServiceResponse<AuthorResponse>> create(
            @Valid @RequestBody AuthorRequest authorRequest) {

        ServiceResponse<AuthorResponse> response = authorService
                .create(mapper.toAuthor(authorRequest))
                .map(mapper::toAuthorResponse);

        return ResponseEntity
                .status(Integer.parseInt(response.getCode()))
                .body(response);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ServiceResponse<AuthorResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody AuthorRequest authorRequest) {

        ServiceResponse<AuthorResponse> response = authorService
                .update(id, mapper.toAuthor(authorRequest))
                .map(mapper::toAuthorResponse);

        return ResponseEntity
                .status(Integer.parseInt(response.getCode()))
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ServiceResponse<Void>> delete(@PathVariable Long id) {

        ServiceResponse<Void> response = authorService.delete(id);

        return ResponseEntity
                .status(Integer.parseInt(response.getCode()))
                .body(response);
    }
}