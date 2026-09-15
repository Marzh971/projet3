package fr.spam.backbookhub.features.loan.respositories;

import fr.spam.backbookhub.features.loan.models.Loan;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class LoanRepositoryJpaAdapter implements ILoanRepository {

    private final LoanJpaRepository loanJpaRepository;

    public LoanRepositoryJpaAdapter(LoanJpaRepository loanJpaRepository) {
        this.loanJpaRepository = loanJpaRepository;
    }

    @Override
    public Optional<Loan> findById(Long id) {
        return loanJpaRepository.findById(id);
    }

    @Override
    public Optional<Loan> findByDateRetrieved(LocalDateTime dateRetrieved) {
        return loanJpaRepository.findByDateRetrieved(dateRetrieved);
    }

    @Override
    public Optional<Loan> findByDateReturned(LocalDateTime dateReturned) {
        return loanJpaRepository.findByDateReturned(dateReturned);
    }

    @Override
    public List<Loan> findAll() {
        return loanJpaRepository.findAll();
    }

    @Override
    public Loan save(Loan loan) {
        return loanJpaRepository.save(loan);
    }

    @Override
    public void deleteById(Long id) {
        loanJpaRepository.deleteById(id);
    }
}