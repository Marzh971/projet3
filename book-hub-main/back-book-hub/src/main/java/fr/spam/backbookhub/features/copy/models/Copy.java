package fr.spam.backbookhub.features.copy.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.spam.backbookhub.features.book.models.Book;
import fr.spam.backbookhub.features.loan.models.Loan;
import fr.spam.backbookhub.features.publisher.models.Publisher;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Copy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 8)
    public String barcode;

    private LocalDateTime purchaseDate;

    @OneToMany(mappedBy = "copy", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonIgnore
    private List<Loan> loans = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "book_id")
    @JsonIgnore
    private Book book;

    @PrePersist
    protected void onCreate() {
        if (this.purchaseDate == null) {
            this.purchaseDate = LocalDateTime.now();
        }
    }
}