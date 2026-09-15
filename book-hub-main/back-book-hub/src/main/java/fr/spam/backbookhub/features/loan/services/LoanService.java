package fr.spam.backbookhub.features.loan.services;

import fr.spam.backbookhub.features.connection.user.models.User;
import fr.spam.backbookhub.features.connection.user.repositories.UserJpaRepository;
import fr.spam.backbookhub.features.copy.models.Copy;
import fr.spam.backbookhub.features.copy.repositories.CopyJpaRepository;
import fr.spam.backbookhub.features.loan.models.Loan;
import fr.spam.backbookhub.features.loan.respositories.ILoanRepository;
import fr.spam.backbookhub.features.loan.respositories.LoanJpaRepository;
import fr.spam.backbookhub.shared.models.ServiceResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LoanService implements ILoanService {

    private final ILoanRepository loanRepository;
    private CopyJpaRepository copyRepository;
    private LoanJpaRepository loanJPARepository;
    private UserJpaRepository userJpaRepository;

    public LoanService(ILoanRepository loanRepository, LoanJpaRepository loanJPARepository, CopyJpaRepository copyRepository, UserJpaRepository userJpaRepository) {
        this.loanRepository = loanRepository;
        this.loanJPARepository = loanJPARepository;
        this.copyRepository = copyRepository;
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public ServiceResponse<Loan> findById(Long id) {
        try {
            return new ServiceResponse<>(
                    "200",
                    "L'emprunt a été trouvé",
                    loanRepository.findById(id).orElse(null)
            );
        } catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Une erreur est survenue lors de la récupération de l'emprunt",
                    null
            );
        }
    }

    @Override
    public ServiceResponse<Loan> findByDateRetrieved(LocalDateTime dateRetrieved) {
        try {
            return new ServiceResponse<>(
                    "200",
                    "L'emprunt a été trouvé",
                    loanRepository.findByDateRetrieved(dateRetrieved).orElse(null)
            );
        } catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Une erreur est survenue lors de la récupération de l'emprunt",
                    null
            );
        }
    }

    @Override
    public ServiceResponse<Loan> findByDateReturned(LocalDateTime dateReturned) {
        try {
            return new ServiceResponse<>(
                    "200",
                    "L'emprunt a été trouvé",
                    loanRepository.findByDateReturned(dateReturned).orElse(null)
            );
        } catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Une erreur est survenue lors de la récupération de l'emprunt",
                    null
            );
        }
    }

    @Override
    public ServiceResponse<List<Loan>> findAll() {
        try {
            return new ServiceResponse<>(
                    "200",
                    "La liste des emprunts a été trouvée",
                    loanRepository.findAll()
            );
        } catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Une erreur est survenue lors de la récupération des emprunts",
                    null
            );
        }
    }

    @Override
    public ServiceResponse<Loan> create(Loan loan) {

        try {
            Loan savedLoan = loanRepository.save(loan);

            return new ServiceResponse<>(
                    "200",
                    "L'emprunt a été créé avec succès",
                    savedLoan
            );

        } catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Une erreur est survenue lors de la création de l'emprunt",
                    null
            );
        }
    }


    @Override
    public ServiceResponse<Loan> createWithLink(Loan loan) {
        try {
            Copy copy = copyRepository.findAvailableCopiesByIsbn(loan.getCopy().getBook().getIsbn())
                    .stream().findFirst().orElseThrow();

            User user = userJpaRepository.findById(loan.getUser().getId())
                    .orElseThrow();
            Loan newLoan = Loan.builder()
                    .copy(copy)
                    .user(user)
                    .dateRetrieved(LocalDateTime.now())
                    .dateReturned(null)
                    .build();

            Loan savedLoan = loanJPARepository.save(newLoan);

            return new ServiceResponse<>(
                    "200",
                    "L'emprunt a été créé avec succès.",
                    savedLoan
            );
        } catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Une erreur est survenue lors de la création de l'emprunt",
                    null
            );
        }
    }


    @Override
    public ServiceResponse<Loan> update(Loan loan) {

        try {

            if (loan.getId() == null) {
                return new ServiceResponse<>(
                        "400",
                        "L'identifiant de l'emprunt est obligatoire",
                        null
                );
            }

            if (loanRepository.findById(loan.getId()).isEmpty()) {
                return new ServiceResponse<>(
                        "404",
                        "L'emprunt n'a pas été trouvé",
                        null
                );
            }

            Loan updatedLoan = loanRepository.save(loan);

            return new ServiceResponse<>(
                    "200",
                    "L'emprunt a été mis à jour",
                    updatedLoan
            );

        } catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Une erreur est survenue lors de la mise à jour de l'emprunt",
                    null
            );
        }
    }

    @Override
    public ServiceResponse<Void> delete(Long id) {
        try {
            loanRepository.deleteById(id);

            return new ServiceResponse<>(
                    "200",
                    "L'emprunt a été supprimé",
                    null
            );

        } catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Une erreur est survenue lors de la suppression de l'emprunt",
                    null
            );
        }
    }

    public boolean isBookAvailable(String isbn) {
        long totalCopies = copyRepository.countByBook_Isbn(isbn);
        long borrowedCopies = loanJPARepository.countByCopy_Book_IsbnAndDateReturnedIsNull(isbn);

        return totalCopies > borrowedCopies;
    }

}