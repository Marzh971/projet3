package fr.spam.backbookhub.features.reservation.repositories;

import fr.spam.backbookhub.features.reservation.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservationJpaRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUserId(UUID idUser);
    List<Reservation> findByBookIsbn(String bookIsbn);

}
