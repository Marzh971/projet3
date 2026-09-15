package fr.spam.backbookhub.features.book.models;

import fr.spam.backbookhub.features.author.models.Author;
import fr.spam.backbookhub.features.category.models.Category;
import fr.spam.backbookhub.features.copy.models.Copy;
import fr.spam.backbookhub.features.publisher.models.Publisher;
import fr.spam.backbookhub.features.reservation.models.Reservation;
import fr.spam.backbookhub.features.review.models.Review;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    private String isbn;
    @Column(nullable = false)
    private String title;

    @Size(max = 2500)
    private String description;
    private String cover;
    private LocalDate publicationDate;
    private int pageNumber;
    @Column(nullable = false)
    @Builder.Default
    private boolean isActive = true;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Review> reviews = new ArrayList<>();
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Reservation> reservations = new ArrayList<>();
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Copy> copies = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    @ManyToMany
    @Builder.Default
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_isbn"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<Author> authors = new HashSet<>();
    @ManyToMany
    @Builder.Default
    @JoinTable(
            name = "book_category",
            joinColumns = @JoinColumn(name = "book_isbn"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();


}
