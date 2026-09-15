package fr.spam.backbookhub.features.loan.respositories;

import fr.spam.backbookhub.features.loan.models.Loan;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ILoanRepository {

    Optional<Loan> findById(Long id);

    Optional<Loan> findByDateRetrieved(LocalDateTime dateRetrieved);

    Optional<Loan> findByDateReturned(LocalDateTime dateReturned);

    List<Loan> findAll();

    Loan save(Loan loan);

    void deleteById(Long id);
}