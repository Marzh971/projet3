package fr.spam.backbookhub.features.reservation.repositories;


import fr.spam.backbookhub.features.reservation.models.Reservation;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ReservationRepositoryJpaAdapter implements IReservationRepository {

    private final ReservationJpaRepository reservationJpaRepository;

    public ReservationRepositoryJpaAdapter(ReservationJpaRepository reservationJpaRepository) {this.reservationJpaRepository = reservationJpaRepository;}

    @Override
    public Optional<Reservation> findById(Long id) {
        return reservationJpaRepository.findById(id);
    }

    @Override
    public List<Reservation> findAll() {
        return reservationJpaRepository.findAll();
    }

    @Override
    public List<Reservation> findUserReservations(UUID idUser) {
        return reservationJpaRepository
                .findByUserId(idUser);
    }

    @Override
    public List<Reservation> findBookReservations(String bookIsbn) {
        return reservationJpaRepository
                .findByBookIsbn(bookIsbn);
    }

    @Override
    public Reservation save(Reservation Reservation) {
        return reservationJpaRepository.save(Reservation);
    }

    @Override
    public void deleteById(Long id) {
        reservationJpaRepository.deleteById(id);
    }
}
