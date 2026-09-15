package fr.spam.backbookhub.features.loan.services;

import fr.spam.backbookhub.features.loan.models.Loan;
import fr.spam.backbookhub.shared.models.ServiceResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface ILoanService {

    ServiceResponse<Loan> findById(Long id);

    ServiceResponse<Loan> findByDateRetrieved(LocalDateTime dateRetrieved);

    ServiceResponse<Loan> findByDateReturned(LocalDateTime dateReturned);

    ServiceResponse<List<Loan>> findAll();

    ServiceResponse<Loan> create(Loan loan);

    ServiceResponse<Loan> createWithLink(Loan loan);

    ServiceResponse<Loan> update(Loan loan);

    ServiceResponse<Void> delete(Long id);
}