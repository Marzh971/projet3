package fr.spam.backbookhub.features.review.services;

import fr.spam.backbookhub.features.book.models.Book;
import fr.spam.backbookhub.features.book.repositories.IBookRepository;
import fr.spam.backbookhub.features.connection.user.models.User;
import fr.spam.backbookhub.features.connection.user.repositories.IUserRepository;
import fr.spam.backbookhub.features.review.models.Review;
import fr.spam.backbookhub.features.review.repositories.IReviewRepository;
import fr.spam.backbookhub.shared.models.ServiceResponse;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService implements IReviewService {

    private final IReviewRepository reviewRepository;
    private final IBookRepository bookRepository;
    private final IUserRepository userRepository;

    public ReviewService(
            IReviewRepository reviewRepository,
            IBookRepository bookRepository,
            IUserRepository userRepository
    ) {
        this.reviewRepository = reviewRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ServiceResponse<Review> findById(Long id) {

        if (id == null) {
            return new ServiceResponse<>(
                    "400",
                    "L'identifiant de la review est obligatoire",
                    null
            );
        }

        try {
            ServiceResponse<Review> response =
                    reviewRepository.findById(id);

            if (!"200".equals(response.getCode()) || response.getData() == null) {
                return new ServiceResponse<>(
                        "404",
                        "La review n'a pas été trouvée",
                        null
                );
            }

            return new ServiceResponse<>(
                    "200",
                    "La review a été trouvée",
                    response.getData()
            );

        } catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Une erreur est survenue lors de la récupération de la review",
                    null
            );
        }
    }

    @Override
    public ServiceResponse<List<Review>> findAll() {

        try {
            return reviewRepository.findAll();

        } catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Une erreur est survenue lors de la récupération des reviews",
                    null
            );
        }
    }

    @Override
    public ServiceResponse<List<Review>> findAllByBook(String bookIsbn) {

        if (bookIsbn == null || bookIsbn.isBlank()) {
            return new ServiceResponse<>(
                    "400",
                    "L'ISBN du livre est obligatoire",
                    null
            );
        }

        try {
            Book book = bookRepository
                    .findById(bookIsbn)
                    .orElse(null);

            if (book == null) {
                return new ServiceResponse<>(
                        "404",
                        "Le livre n'a pas été trouvé",
                        null
                );
            }

            return reviewRepository.findAllByBook(book);

        } catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Une erreur est survenue lors de la récupération des reviews",
                    null
            );
        }
    }

    @Override
    public ServiceResponse<Review> create(
            String bookIsbn,
            Review review
    ) {

        if (bookIsbn == null || bookIsbn.isBlank()) {
            return new ServiceResponse<>(
                    "400",
                    "L'ISBN du livre est obligatoire",
                    null
            );
        }

        if (review == null) {
            return new ServiceResponse<>(
                    "400",
                    "La review est obligatoire",
                    null
            );
        }

        try {
            Authentication authentication =
                    SecurityContextHolder
                            .getContext()
                            .getAuthentication();


            if (authentication == null
                    || !authentication.isAuthenticated()
                    || authentication instanceof AnonymousAuthenticationToken) {

                return new ServiceResponse<>(
                        "401",
                        "Vous devez être connecté pour laisser un avis",
                        null
                );
            }

            User user = userRepository
                    .findByEmail(authentication.getName())
                    .orElse(null);

            if (user == null) {
                return new ServiceResponse<>(
                        "404",
                        "Utilisateur connecté introuvable",
                        null
                );
            }

            Book book = bookRepository
                    .findById(bookIsbn)
                    .orElse(null);

            if (book == null) {
                return new ServiceResponse<>(
                        "404",
                        "Le livre n'a pas été trouvé",
                        null
                );
            }

            boolean alreadyReviewed =
                    reviewRepository.existsByUserAndBook(
                            user,
                            book
                    );

            if (alreadyReviewed) {
                return new ServiceResponse<>(
                        "409",
                        "Vous avez déjà laissé un avis pour ce livre",
                        null
                );
            }

            review.setUser(user);
            review.setBook(book);
            review.setDateReview(LocalDateTime.now());

            ServiceResponse<Review> saveResponse =
                    reviewRepository.save(review);

            return new ServiceResponse<>(
                    "201",
                    "L'avis a été ajouté avec succès",
                    saveResponse.getData()
            );

        } catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Une erreur est survenue lors de la création de l'avis",
                    null
            );
        }
    }

    @Override
    public ServiceResponse<Review> update(
            Long id,
            Review review
    ) {

        if (id == null) {
            return new ServiceResponse<>(
                    "400",
                    "L'identifiant de la review est obligatoire",
                    null
            );
        }

        if (review == null) {
            return new ServiceResponse<>(
                    "400",
                    "La review est obligatoire",
                    null
            );
        }

        try {
            Authentication authentication =
                    SecurityContextHolder
                            .getContext()
                            .getAuthentication();

            if (authentication == null
                    || !authentication.isAuthenticated()
                    || authentication instanceof AnonymousAuthenticationToken) {

                return new ServiceResponse<>(
                        "401",
                        "Vous devez être connecté",
                        null
                );
            }

            User user = userRepository
                    .findByEmail(authentication.getName())
                    .orElse(null);

            if (user == null) {
                return new ServiceResponse<>(
                        "404",
                        "Utilisateur connecté introuvable",
                        null
                );
            }

            ServiceResponse<Review> response =
                    reviewRepository.findById(id);

            Review existingReview = response.getData();

            if (existingReview == null) {
                return new ServiceResponse<>(
                        "404",
                        "La review n'a pas été trouvée",
                        null
                );
            }

            if (existingReview.getUser() == null
                    || !existingReview.getUser().getId().equals(user.getId())) {

                return new ServiceResponse<>(
                        "403",
                        "Vous ne pouvez modifier que vos propres avis",
                        null
                );
            }

            existingReview.setRate(review.getRate());
            existingReview.setComment(review.getComment());

            ServiceResponse<Review> saveResponse =
                    reviewRepository.save(existingReview);

            return new ServiceResponse<>(
                    "200",
                    "L'avis a été modifié avec succès",
                    saveResponse.getData()
            );

        } catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Une erreur est survenue lors de la modification de l'avis",
                    null
            );
        }
    }

    @Override
    public ServiceResponse<Void> delete(Long id) {

        if (id == null) {
            return new ServiceResponse<>(
                    "400",
                    "L'identifiant de la review est obligatoire",
                    null
            );
        }

        try {
            Authentication authentication =
                    SecurityContextHolder
                            .getContext()
                            .getAuthentication();

            if (authentication == null
                    || !authentication.isAuthenticated()
                    || authentication instanceof AnonymousAuthenticationToken) {

                return new ServiceResponse<>(
                        "401",
                        "Vous devez être connecté",
                        null
                );
            }

            User user = userRepository
                    .findByUsername(authentication.getName())
                    .orElse(null);

            if (user == null) {
                return new ServiceResponse<>(
                        "404",
                        "Utilisateur connecté introuvable",
                        null
                );
            }

            ServiceResponse<Review> response =
                    reviewRepository.findById(id);

            Review review = response.getData();

            if (review == null) {
                return new ServiceResponse<>(
                        "404",
                        "La review n'a pas été trouvée",
                        null
                );
            }

            if (review.getUser() == null
                    || !review.getUser().getId().equals(user.getId())) {

                return new ServiceResponse<>(
                        "403",
                        "Vous ne pouvez supprimer que vos propres avis",
                        null
                );
            }

            return reviewRepository.deleteById(id);

        } catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Une erreur est survenue lors de la suppression de l'avis",
                    null
            );
        }
    }
}
