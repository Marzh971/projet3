package fr.spam.backbookhub.features.review.repositories;

import fr.spam.backbookhub.features.book.models.Book;
import fr.spam.backbookhub.features.connection.user.models.User;
import fr.spam.backbookhub.features.review.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewJpaRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByBook(Book book);

    boolean existsByUserAndBook(User user, Book book);
}

