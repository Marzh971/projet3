package fr.spam.backbookhub.features.review.api.mappers;

import fr.spam.backbookhub.features.review.api.dtos.ReviewListResponse;
import fr.spam.backbookhub.features.review.api.dtos.ReviewRequest;
import fr.spam.backbookhub.features.review.api.dtos.ReviewResponse;
import fr.spam.backbookhub.features.review.api.dtos.ReviewUpdateRequest;
import fr.spam.backbookhub.features.review.models.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(target = "book_isbn", source = "book.isbn")
    @Mapping(target = "username", source = "user.username")
    ReviewListResponse toReviewListResponse(Review review);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateReview", ignore = true)
    @Mapping(target = "book", ignore = true)
    @Mapping(target = "user", ignore = true)
    Review toReview(ReviewRequest reviewRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateReview", ignore = true)
    @Mapping(target = "book", ignore = true)
    @Mapping(target = "user", ignore = true)
    Review toReview(ReviewUpdateRequest reviewUpdateRequest);

    @Mapping(target = "bookIsbn", source = "book.isbn")
    @Mapping(target = "username", source = "user.username")
    ReviewResponse toReviewResponse(Review review);
}