package fr.spam.backbookhub.features.reservation.api.controllers;

import fr.spam.backbookhub.features.connection.user.models.User;
import fr.spam.backbookhub.features.connection.user.repositories.IUserRepository;
import fr.spam.backbookhub.features.reservation.api.dtos.ReservationRequest;
import fr.spam.backbookhub.features.reservation.api.dtos.ReservationResponse;
import fr.spam.backbookhub.features.reservation.services.IReservationService;
import fr.spam.backbookhub.shared.models.ServiceResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final IReservationService reservationService;
    private final IUserRepository userRepository;
    @PreAuthorize("hasRole('LIBRARIAN')")
    @GetMapping("")
    public ResponseEntity<ServiceResponse<List<ReservationResponse>>> findAll() {
        ServiceResponse<List<ReservationResponse>> response = reservationService.findAll();
        return ResponseEntity.status(Integer.parseInt(response.getCode())).body(response);
    }
    @PreAuthorize("hasRole('LIBRARIAN')")
    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponse<ReservationResponse>> findById(@PathVariable Long id) {
        ServiceResponse<ReservationResponse> response = reservationService.findById(id);
        return ResponseEntity.status(Integer.parseInt(response.getCode())).body(response);
    }
    @PreAuthorize("hasRole('LIBRARIAN')")
    @GetMapping("/users/{idUser}")
    public ResponseEntity<ServiceResponse<List<ReservationResponse>>> findUserReservations(@PathVariable UUID idUser) {
        ServiceResponse<List<ReservationResponse>> response = reservationService.findUserReservations(idUser);
        return ResponseEntity.status(Integer.parseInt(response.getCode())).body(response);
    }
    @PreAuthorize("hasRole('LIBRARIAN')")
    @GetMapping("/books/{bookId}")
    public ResponseEntity<ServiceResponse<List<ReservationResponse>>> findBookReservations(@PathVariable String bookId) {
        ServiceResponse<List<ReservationResponse>> response = reservationService.findBookReservations(bookId);
        return ResponseEntity.status(Integer.parseInt(response.getCode())).body(response);
    }
    @PreAuthorize("hasRole('LIBRARIAN')")
    @PostMapping("")
    public ResponseEntity<ServiceResponse<ReservationResponse>> create(
            @Valid @RequestBody ReservationRequest reservationRequest,
            @AuthenticationPrincipal UserDetails userDetails) {

        User currentUser = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException("Utilisateur authentifié introuvable"));

        // On ignore le userId envoyé par le client : seul l'utilisateur connecté compte
        reservationRequest.setUserId(currentUser.getId());

        ServiceResponse<ReservationResponse> response = reservationService.create(reservationRequest);
        return ResponseEntity.status(Integer.parseInt(response.getCode())).body(response);
    }
    @PreAuthorize("hasRole('LIBRARIAN')")
    @PutMapping("/{id}")
    public ResponseEntity<ServiceResponse<ReservationResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody ReservationRequest reservationRequest) {
        ServiceResponse<ReservationResponse> response = reservationService.update(id, reservationRequest);
        return ResponseEntity.status(Integer.parseInt(response.getCode())).body(response);
    }
    @PreAuthorize("hasRole('LIBRARIAN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ServiceResponse<Void>> delete(@PathVariable Long id) {
        ServiceResponse<Void> response = reservationService.delete(id);
        return ResponseEntity.status(Integer.parseInt(response.getCode())).body(response);
    }
}
