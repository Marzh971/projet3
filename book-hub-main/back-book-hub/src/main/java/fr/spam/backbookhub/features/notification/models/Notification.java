package fr.spam.backbookhub.features.notification.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime dateNotification;
    private String text;
    @Column(nullable = false)
    private boolean isRead = false;
    @Column(length = 50)
    private String typeNotification;


}