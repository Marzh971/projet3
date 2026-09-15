package fr.spam.backbookhub.integration.services;

import fr.spam.backbookhub.features.book.models.Book;
import fr.spam.backbookhub.features.book.repositories.IBookRepository;
import fr.spam.backbookhub.features.book.services.BookService;
import fr.spam.backbookhub.shared.models.ServiceResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class BookServicesTest {
    @Mock
    private IBookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void getBookByIsbn(){
        Book book = Book.builder().isbn("9780151660384").build();
        when(bookRepository.findById("9780151660384")).thenReturn(Optional.of(book));

        ServiceResponse<Book> response = bookService.findByIsbn("9780151660384");
        assertEquals("200", response.getCode());
        assertEquals(book, response.getData());
    }

    @Test
    void getActivesBook(){
        Book book1 = Book.builder().isActive(true).build();
        Book book2 = Book.builder().isActive(true).build();
        when(bookRepository.findAllByIsActiveIsTrue())
                .thenReturn(List.of(book1,book2));

        ServiceResponse<List<Book>> response = bookService.findAllActive();

        response.getData().forEach(book -> assertTrue(book.isActive()));
    }

}
