package fr.spam.backbookhub.features.category.models;

import fr.spam.backbookhub.features.book.models.Book;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nameCategory;

    @ManyToMany(mappedBy = "categories")
    @Builder.Default
    @ToString.Exclude
    private Set<Book> books = new HashSet<>();
}