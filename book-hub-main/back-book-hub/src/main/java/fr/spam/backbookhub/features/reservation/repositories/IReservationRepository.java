package fr.spam.backbookhub.features.reservation.repositories;

import fr.spam.backbookhub.features.reservation.models.Reservation;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IReservationRepository {
    
    // CRUD operations
    Optional<Reservation> findById(Long id);
    List<Reservation> findAll();
    List<Reservation> findUserReservations(UUID idUser);
    List<Reservation> findBookReservations(String bookId);
    Reservation save(Reservation Reservation);
    void deleteById(Long id);
}
 