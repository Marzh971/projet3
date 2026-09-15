package fr.spam.backbookhub.features.book.repositories;

import fr.spam.backbookhub.features.book.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface IBookRepository extends JpaRepository<Book, String>, JpaSpecificationExecutor<Book> {

    List<Book> findAllByIsActiveIsTrue();
}
