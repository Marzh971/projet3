package fr.spam.backbookhub.features.review.api.controllers;

import fr.spam.backbookhub.features.connection.user.services.UserService;
import fr.spam.backbookhub.features.review.api.dtos.ReviewListResponse;
import fr.spam.backbookhub.features.review.api.dtos.ReviewRequest;
import fr.spam.backbookhub.features.review.api.dtos.ReviewResponse;
import fr.spam.backbookhub.features.review.api.dtos.ReviewUpdateRequest;
import fr.spam.backbookhub.features.review.api.mappers.ReviewMapper;
import fr.spam.backbookhub.features.review.models.Review;
import fr.spam.backbookhub.features.review.services.IReviewService;
import fr.spam.backbookhub.shared.models.ServiceResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/review")
public class ReviewController {

    private final IReviewService reviewService;
    private final ReviewMapper mapper;

    public ReviewController(
            IReviewService reviewService,
            ReviewMapper mapper) {
        this.reviewService = reviewService;
        this.mapper = mapper;
    }

    @GetMapping("")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ServiceResponse<List<ReviewListResponse>>> findAll() {

        ServiceResponse<List<ReviewListResponse>> response =
                reviewService
                        .findAll()
                        .map(reviews ->
                                reviews.stream()
                                        .map(mapper::toReviewListResponse)
                                        .toList()
                        );

        return ResponseEntity
                .status(Integer.parseInt(response.getCode()))
                .body(response);
    }

    @PostMapping("")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ServiceResponse<ReviewResponse>> create(
            @Valid @RequestBody ReviewRequest reviewRequest,
            Principal principal
    ) {


        Review review = mapper.toReview(reviewRequest);

        ServiceResponse<ReviewResponse> response =
                reviewService
                        .create(
                                reviewRequest.bookIsbn(),
                                review
                        )
                        .map(mapper::toReviewResponse);

        return ResponseEntity
                .status(Integer.parseInt(response.getCode()))
                .body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ServiceResponse<ReviewResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody ReviewUpdateRequest reviewUpdateRequest
    ) {

        Review review = mapper.toReview(reviewUpdateRequest);

        ServiceResponse<ReviewResponse> response =
                reviewService
                        .update(id, review)
                        .map(mapper::toReviewResponse);

        return ResponseEntity
                .status(Integer.parseInt(response.getCode()))
                .body(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ServiceResponse<Void>> delete(
            @PathVariable Long id
    ) {

        ServiceResponse<Void> response =
                reviewService.delete(id);

        return ResponseEntity
                .status(Integer.parseInt(response.getCode()))
                .body(response);
    }
}