package fr.spam.backbookhub.features.reservation.api.dtos;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponse {
    private Long id;
    private LocalDateTime dateReservation;
    private String stateReservation;

    private String bookTitle;

    private String username;

}
