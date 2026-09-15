package fr.spam.backbookhub.features.copy.services;

import fr.spam.backbookhub.features.book.models.Book;
import fr.spam.backbookhub.features.copy.models.Copy;
import fr.spam.backbookhub.shared.models.ServiceResponse;

import java.util.List;

public interface ICopyService {
    ServiceResponse<List<Copy>> findAll();
    ServiceResponse<List<Copy>> findAllByBook(Book book);
    ServiceResponse<Copy> findById(Long id);
    ServiceResponse<Copy> findByBarcode(String barcode);
    ServiceResponse<Copy> create(Copy copy);
    ServiceResponse<Void> delete(Long id);

    ServiceResponse<Copy> findFirstAvailableCopy(String isbn);
}
