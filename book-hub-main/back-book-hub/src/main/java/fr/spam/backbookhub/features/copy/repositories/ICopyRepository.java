package fr.spam.backbookhub.features.copy.repositories;

import fr.spam.backbookhub.features.book.models.Book;
import fr.spam.backbookhub.features.copy.models.Copy;
import fr.spam.backbookhub.features.loan.models.Loan;

import java.util.List;
import java.util.Optional;

public interface ICopyRepository {
    List<Copy> findAll();
    List<Copy> findAllByBook(Book book);
    Optional<Copy> findById(Long id);
    Optional<Copy> findByBarcode(String barcode);
    Copy save(Copy copy);
    void deleteById(Long id);
}
