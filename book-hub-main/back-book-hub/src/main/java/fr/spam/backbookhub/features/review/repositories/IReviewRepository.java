package fr.spam.backbookhub.features.review.repositories;

import fr.spam.backbookhub.features.book.models.Book;
import fr.spam.backbookhub.features.connection.user.models.User;
import fr.spam.backbookhub.features.review.models.Review;
import fr.spam.backbookhub.shared.models.ServiceResponse;

import java.util.List;

public interface IReviewRepository {

    ServiceResponse<Review> findById(Long id);

    ServiceResponse<List<Review>> findAll();

    ServiceResponse<List<Review>> findAllByBook(Book book);

    ServiceResponse<Review> save(Review review);

    ServiceResponse<Void> deleteById(Long id);

    boolean existsByUserAndBook(User user, Book book);
}
