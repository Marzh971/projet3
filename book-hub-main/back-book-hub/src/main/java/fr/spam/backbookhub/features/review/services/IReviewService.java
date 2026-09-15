package fr.spam.backbookhub.features.review.services;

import fr.spam.backbookhub.features.review.models.Review;
import fr.spam.backbookhub.shared.models.ServiceResponse;

import java.util.List;

public interface IReviewService {

    ServiceResponse<Review> findById(Long id);

    ServiceResponse<List<Review>> findAll();

    ServiceResponse<List<Review>> findAllByBook(String bookIsbn);

    ServiceResponse<Review> create(String bookIsbn, Review review);

    ServiceResponse<Review> update(Long id, Review review);

    ServiceResponse<Void> delete(Long id);
}