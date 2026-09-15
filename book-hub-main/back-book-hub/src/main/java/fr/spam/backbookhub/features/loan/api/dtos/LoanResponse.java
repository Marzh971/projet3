package fr.spam.backbookhub.features.loan.api.dtos;

import java.time.LocalDateTime;
import java.util.List;

public record LoanResponse(

        Long id,
        LocalDateTime dateRetrieved,
        LocalDateTime dateReturned,
        List<LoanListResponse> loans

) {
}