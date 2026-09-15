package fr.spam.backbookhub.features.reservation.models;

import fr.spam.backbookhub.features.book.models.Book;
import fr.spam.backbookhub.features.connection.user.models.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @Getter
    @Setter
    @Builder
    public class Reservation {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private LocalDateTime dateReservation;
        private String stateReservation;

        @ManyToOne
        @JoinColumn(name = "book_isbn", nullable = false)
        private Book book;

        @ManyToOne
        @JoinColumn(name = "user_id", nullable = false)
        private User user;



    }
