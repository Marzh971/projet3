package fr.spam.backbookhub.features.loan.respositories;


import fr.spam.backbookhub.features.loan.models.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface LoanJpaRepository extends JpaRepository<Loan, Long> {

    Optional<Loan> findByDateRetrieved(LocalDateTime dateRetrieved);

    Optional<Loan> findByDateReturned(LocalDateTime dateReturned);

    long countByCopy_Book_IsbnAndDateReturnedIsNull(String copyBookIsbn);
}