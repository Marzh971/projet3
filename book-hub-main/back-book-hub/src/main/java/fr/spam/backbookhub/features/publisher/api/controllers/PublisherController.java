package fr.spam.backbookhub.features.publisher.api.controllers;

import fr.spam.backbookhub.features.publisher.api.dtos.PublisherListResponse;
import fr.spam.backbookhub.features.publisher.api.dtos.PublisherRequest;
import fr.spam.backbookhub.features.publisher.api.dtos.PublisherResponse;
import fr.spam.backbookhub.features.publisher.api.mappers.PublisherMapper;
import fr.spam.backbookhub.features.publisher.services.IPublisherService;
import fr.spam.backbookhub.shared.models.ServiceResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publisher")
public class PublisherController {

    private final IPublisherService publisherService;
    private final PublisherMapper mapper;

    public PublisherController(IPublisherService publisherService, PublisherMapper mapper) {
        this.publisherService = publisherService;
        this.mapper = mapper;
    }

    @GetMapping("")
    public ResponseEntity<ServiceResponse<List<PublisherListResponse>>> findAll() {
        return ResponseEntity
                .status(200)
                .body(
                        publisherService
                                .findAll()
                                .map(publishers -> publishers
                                        .stream()
                                        .map(mapper::toPublisherListResponse)
                                        .toList())
                );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponse<PublisherResponse>> findById(@PathVariable Long id) {

        ServiceResponse<PublisherResponse> response = publisherService
                .findById(id)
                .map(mapper::toPublisherResponse);

        return ResponseEntity
                .status(Integer.parseInt(response.getCode()))
                .body(response);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ServiceResponse<PublisherResponse>> findByName(@PathVariable String name) {

        ServiceResponse<PublisherResponse> response = publisherService
                .findByName(name)
                .map(mapper::toPublisherResponse);

        return ResponseEntity
                .status(Integer.parseInt(response.getCode()))
                .body(response);
    }


    @PostMapping("")
    public ResponseEntity<ServiceResponse<PublisherResponse>> create(
            @Valid @RequestBody PublisherRequest publisherRequest) {

        ServiceResponse<PublisherResponse> response = publisherService
                .create(mapper.toPublisher(publisherRequest))
                .map(mapper::toPublisherResponse);

        return ResponseEntity
                .status(Integer.parseInt(response.getCode()))
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceResponse<PublisherResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody PublisherRequest publisherRequest) {

        ServiceResponse<PublisherResponse> response = publisherService
                .update(id, mapper.toPublisher(publisherRequest))
                .map(mapper::toPublisherResponse);

        return ResponseEntity
                .status(Integer.parseInt(response.getCode()))
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ServiceResponse<Void>> delete(@PathVariable Long id) {

        ServiceResponse<Void> response = publisherService.delete(id);

        return ResponseEntity
                .status(Integer.parseInt(response.getCode()))
                .body(response);
    }
}