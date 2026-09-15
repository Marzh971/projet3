package fr.spam.backbookhub.features.review.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.spam.backbookhub.features.book.models.Book;
import fr.spam.backbookhub.features.connection.user.models.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "review",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_review_user_book",
                        columnNames = {"user_id", "book_isbn"}
                )
        }
)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(1)
    @Max(5)
    @Column(nullable = false)
    private int rate;

    @Column(nullable = false)
    private String comment;

    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime dateReview = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_isbn", nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
