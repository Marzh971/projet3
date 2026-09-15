package fr.spam.backbookhub.features.reservation.services;

import fr.spam.backbookhub.features.book.models.Book;
import fr.spam.backbookhub.features.book.repositories.IBookRepository;
import fr.spam.backbookhub.features.connection.user.models.User;
import fr.spam.backbookhub.features.connection.user.repositories.IUserRepository;
import fr.spam.backbookhub.features.copy.repositories.ICopyRepository;
import fr.spam.backbookhub.features.reservation.api.dtos.ReservationRequest;
import fr.spam.backbookhub.features.reservation.api.dtos.ReservationResponse;
import fr.spam.backbookhub.features.reservation.models.Reservation;
import fr.spam.backbookhub.features.reservation.models.ReservationState;
import fr.spam.backbookhub.features.reservation.repositories.IReservationRepository;
import fr.spam.backbookhub.shared.models.ServiceResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class ReservationService implements IReservationService {

    private final IReservationRepository reservationRepository;
    private final IBookRepository bookRepository;
    private final IUserRepository userRepository;
    private final ICopyRepository copyRepository;

    @Override
    public ServiceResponse<ReservationResponse> findById(Long id) {
        try {
            Reservation reservation = reservationRepository.findById(id).orElse(null);
            return new ServiceResponse<>("200", "La réservation a été trouvée", toResponse(reservation));
        } catch (Exception e) {
            return new ServiceResponse<>("500", "Une erreur est survenue lors de la récupération de la réservation", null);
        }
    }

    @Override
    public ServiceResponse<List<ReservationResponse>> findUserReservations(UUID idUser) {
        try {
            List<ReservationResponse> reservations = reservationRepository.findUserReservations(idUser)
                    .stream().map(this::toResponse).toList();
            return new ServiceResponse<>("200", "La liste des réservations de l'utilisateur a été trouvée", reservations);
        } catch (Exception e) {
            return new ServiceResponse<>("500", "Une erreur est survenue lors de la récupération des réservations", null);
        }
    }

    @Override
    public ServiceResponse<List<ReservationResponse>> findBookReservations(String bookId) {
        try {
            List<ReservationResponse> reservations = reservationRepository.findBookReservations(bookId)
                    .stream().map(this::toResponse).toList();
            return new ServiceResponse<>("200", "La liste des réservations du livre a été trouvée", reservations);
        } catch (Exception e) {
            return new ServiceResponse<>("500", "Une erreur est survenue lors de la récupération des réservations", null);
        }
    }

    @Override
    public ServiceResponse<List<ReservationResponse>> findAll() {
        try {
            List<ReservationResponse> reservations = reservationRepository.findAll()
                    .stream().map(this::toResponse).toList();
            return new ServiceResponse<>("200", "La liste des réservations a été trouvée", reservations);
        } catch (Exception e) {
            return new ServiceResponse<>("500", "Une erreur est survenue lors de la récupération des réservations", null);
        }
    }

    @Override
    public ServiceResponse<ReservationResponse> create(ReservationRequest request) {
        try {
            Book book = bookRepository.findById(request.getBookIsbn()).orElse(null);
            if (book == null) {
                return new ServiceResponse<>("404", "Le livre n'a pas été trouvé", null);
            }

            User user = userRepository.findById(request.getUserId()).orElse(null);
            if (user == null) {
                return new ServiceResponse<>("404", "L'utilisateur n'a pas été trouvé", null);
            }

            List<Reservation> reservationsUser = reservationRepository.findUserReservations(user.getId());

            long reservationsEnCours = reservationsUser.stream()
                    .filter(r -> ReservationState.EN_ATTENTE.equals(r.getStateReservation()))
                    .count();
            if (reservationsEnCours >= 5) {
                return new ServiceResponse<>("409", "Vous avez déjà 5 réservations en cours", null);
            }

            long empruntsEnCours = reservationsUser.stream()
                    .filter(r -> ReservationState.EMPRUNTE.equals(r.getStateReservation()))
                    .count();
            if (empruntsEnCours >= 3) {
                return new ServiceResponse<>("409", "Vous avez déjà 3 emprunts en cours", null);
            }

            Reservation reservation = Reservation.builder()
                    .dateReservation(LocalDateTime.now())
                    .stateReservation(ReservationState.EN_ATTENTE) // état forcé, jamais celui du client
                    .book(book)
                    .user(user)
                    .build();

            Reservation savedReservation = reservationRepository.save(reservation);

            return new ServiceResponse<>("201", "La réservation a été créée", toResponse(savedReservation));

        } catch (Exception e) {
            return new ServiceResponse<>("500", "Une erreur est survenue lors de la création de la réservation", null);
        }
    }

    @Override
    public ServiceResponse<ReservationResponse> update(Long id, ReservationRequest request) {
        try {
            Reservation reservation = reservationRepository.findById(id).orElse(null);
            if (reservation == null) {
                return new ServiceResponse<>("404", "La réservation n'a pas été trouvée", null);
            }
            reservation.setStateReservation(request.getStateReservation());
            Reservation updatedReservation = reservationRepository.save(reservation);
            return new ServiceResponse<>("200", "La réservation a été mise à jour", toResponse(updatedReservation));
        } catch (Exception e) {
            return new ServiceResponse<>("500", "Une erreur est survenue lors de la mise à jour de la réservation", null);
        }
    }

    @Override
    public ServiceResponse<Void> delete(Long id) {
        try {
            reservationRepository.deleteById(id);
            return new ServiceResponse<>("200", "La réservation a été supprimée", null);
        } catch (Exception e) {
            return new ServiceResponse<>("500", "Une erreur est survenue lors de la suppression de la réservation", null);
        }
    }


    private ReservationResponse toResponse(Reservation reservation) {
        if (reservation == null) {
            return null;
        }
        return ReservationResponse.builder()
                .id(reservation.getId())
                .dateReservation(reservation.getDateReservation())
                .stateReservation(reservation.getStateReservation())
                .bookTitle(reservation.getBook().getTitle())
                .username(reservation.getUser().getUsername())
                .build();
    }
}