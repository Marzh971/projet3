package fr.spam.backbookhub.features.review.repositories;

import fr.spam.backbookhub.features.book.models.Book;
import fr.spam.backbookhub.features.connection.user.models.User;
import fr.spam.backbookhub.features.review.models.Review;
import fr.spam.backbookhub.shared.models.ServiceResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReviewRepositoryJpaAdapter implements IReviewRepository {

    private final ReviewJpaRepository reviewJpaRepository;

    public ReviewRepositoryJpaAdapter(
            ReviewJpaRepository reviewJpaRepository
    ) {
        this.reviewJpaRepository = reviewJpaRepository;
    }

    @Override
    public ServiceResponse<Review> findById(Long id) {
        try {
            Review review = reviewJpaRepository
                    .findById(id)
                    .orElse(null);

            if (review == null) {
                return new ServiceResponse<>(
                        "404",
                        "La review n'a pas été trouvée",
                        null
                );
            }

            return new ServiceResponse<>(
                    "200",
                    "La review a été trouvée",
                    review
            );

        } catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Erreur lors de la récupération de la review",
                    null
            );
        }
    }

    @Override
    public ServiceResponse<List<Review>> findAll() {
        try {
            return new ServiceResponse<>(
                    "200",
                    "Les reviews ont été trouvées",
                    reviewJpaRepository.findAll()
            );

        } catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Erreur lors de la récupération des reviews",
                    null
            );
        }
    }

    @Override
    public ServiceResponse<List<Review>> findAllByBook(Book book) {
        try {
            return new ServiceResponse<>(
                    "200",
                    "Les reviews du livre ont été trouvées",
                    reviewJpaRepository.findAllByBook(book)
            );

        } catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Erreur lors de la récupération des reviews du livre",
                    null
            );
        }
    }

    @Override
    public ServiceResponse<Review> save(Review review) {
        try {
            Review savedReview = reviewJpaRepository.save(review);

            return new ServiceResponse<>(
                    "200",
                    "La review a été enregistrée",
                    savedReview
            );

        } catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Erreur lors de l'enregistrement de la review",
                    null
            );
        }
    }

    @Override
    public ServiceResponse<Void> deleteById(Long id) {
        try {
            if (!reviewJpaRepository.existsById(id)) {
                return new ServiceResponse<>(
                        "404",
                        "La review n'a pas été trouvée",
                        null
                );
            }

            reviewJpaRepository.deleteById(id);

            return new ServiceResponse<>(
                    "200",
                    "La review a été supprimée",
                    null
            );

        } catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Erreur lors de la suppression de la review",
                    null
            );
        }
    }

    @Override
    public boolean existsByUserAndBook(
            User user,
            Book book
    ) {
        return reviewJpaRepository.existsByUserAndBook(
                user,
                book
        );
    }
}
