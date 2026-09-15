package fr.spam.backbookhub.features.copy.repositories;

import fr.spam.backbookhub.features.book.models.Book;
import fr.spam.backbookhub.features.copy.models.Copy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CopyJpaRepository extends JpaRepository<Copy, Long> {
    List<Copy> findAllByBook(Book book);
    Optional<Copy> findByBarcode(String barcode);

    long countByBook_Isbn(String bookIsbn);


    @Query("""
        SELECT c FROM Copy c
        WHERE c.book.isbn = :isbn
        AND NOT EXISTS (
            SELECT l FROM Loan l
            WHERE l.copy = c AND l.dateReturned IS NULL
        )
        """)
    List<Copy> findAvailableCopiesByIsbn(@Param("isbn") String isbn);

}
