package fr.spam.backbookhub.features.reservation.services;

import fr.spam.backbookhub.features.book.models.Book;
import fr.spam.backbookhub.features.reservation.api.dtos.ReservationRequest;
import fr.spam.backbookhub.features.reservation.api.dtos.ReservationResponse;
import fr.spam.backbookhub.shared.models.ServiceResponse;

import java.util.List;
import java.util.UUID;

public interface IReservationService {
    ServiceResponse<ReservationResponse> findById(Long id);
    ServiceResponse<List<ReservationResponse>> findUserReservations(UUID idUser);
    ServiceResponse<List<ReservationResponse>> findBookReservations(String bookId);
    ServiceResponse<List<ReservationResponse>> findAll();
    ServiceResponse<ReservationResponse> create(ReservationRequest reservationRequest);
    ServiceResponse<ReservationResponse> update(Long id, ReservationRequest reservationRequest);
    ServiceResponse<Void> delete(Long id);
}


