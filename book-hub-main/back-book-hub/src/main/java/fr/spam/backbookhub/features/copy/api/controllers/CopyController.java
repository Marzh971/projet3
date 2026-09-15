package fr.spam.backbookhub.features.copy.api.controllers;

import fr.spam.backbookhub.features.copy.api.dtos.CopyListResponse;
import fr.spam.backbookhub.features.copy.api.dtos.CopyRequest;
import fr.spam.backbookhub.features.copy.api.dtos.CopyResponse;
import fr.spam.backbookhub.features.copy.api.mappers.CopyMapper;
import fr.spam.backbookhub.features.copy.services.ICopyService;
import fr.spam.backbookhub.shared.models.ServiceResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/copies")
public class CopyController {

    ICopyService copyService;
    CopyMapper mapper;

    public CopyController(ICopyService copyService, CopyMapper mapper) {
        this.copyService = copyService;
        this.mapper = mapper;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ServiceResponse<List<CopyListResponse>>> getCopies() {
        return ResponseEntity
                .status(200)
                .body(
                        copyService
                                .findAll()
                                .map(copies -> copies
                                        .stream()
                                        .map(mapper::toCopyListResponse)
                                        .toList())
                );
    }

    @GetMapping("/barcode/{barcode}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ServiceResponse<CopyResponse>> getCopyByBarcode(@PathVariable String barcode) {
        return ResponseEntity
                .status(200)
                .body(
                        copyService
                                .findByBarcode(barcode)
                                .map(mapper::toCopyResponse)
                );
    }



    @GetMapping("/isbn/{isbn}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ServiceResponse<CopyResponse>> getFirstCopyAvailableByIsbn(@PathVariable String isbn) {
        return ResponseEntity
                .status(200)
                .body(
                        copyService
                                .findFirstAvailableCopy(isbn)
                                .map(mapper::toCopyResponse)
                );
    }


    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ServiceResponse<CopyResponse>> getCopyById(@PathVariable Long id) {
        return ResponseEntity
                .status(200)
                .body(
                        copyService
                                .findById(id)
                                .map(mapper::toCopyResponse)
                );
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ServiceResponse<CopyResponse>> createCopy(@Valid @RequestBody CopyRequest request) {
        return ResponseEntity
                .status(201)
                .body(
                        copyService
                                .create(mapper.toCopy(request))
                                .map(mapper::toCopyResponse)
                );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ServiceResponse<Void>> deleteCopy(@PathVariable Long id) {
        return ResponseEntity
                .status(200)
                .body(
                        copyService
                                .delete(id)
                );
    }
}