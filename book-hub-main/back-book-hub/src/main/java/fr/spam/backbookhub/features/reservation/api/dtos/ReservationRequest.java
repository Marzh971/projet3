package fr.spam.backbookhub.features.reservation.api.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequest{

    private String stateReservation;

    private UUID userId;

    @NotNull(message = "Le livre est obligatoire")
    private String bookIsbn;
}
