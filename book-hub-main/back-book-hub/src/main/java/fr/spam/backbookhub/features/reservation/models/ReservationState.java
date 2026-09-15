package fr.spam.backbookhub.features.reservation.models;

import java.util.List;

public final class ReservationState {
    public static final String EN_ATTENTE = "EN_ATTENTE"; // réservé, en attente de retrait
    public static final String EMPRUNTE = "EMPRUNTE";     // livre physiquement sorti
    public static final String ANNULEE = "ANNULEE";

    public static final List<String> ETATS_ACTIFS = List.of(EN_ATTENTE, EMPRUNTE);

    private ReservationState() {}
}
