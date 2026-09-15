package fr.spam.backbookhub.unite;

import fr.spam.backbookhub.features.book.models.Book;
import fr.spam.backbookhub.features.book.services.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class BookTest {

    @Test
    void createBookTest() {
        Book bookTest = Book.builder()
                .isbn("9782070409189")
                .title("Les Misérables")
                .description("Roman historique de Victor Hugo.")
                .publicationDate(LocalDate.parse("1862-01-01"))
                .pageNumber(1463)
                .isActive(true)
                .build();

        assertThat(bookTest).isNotNull();
        assertThat(bookTest.getIsbn()).isEqualTo("9782070409189");
        assertThat(bookTest.getTitle()).isEqualTo("Les Misérables");
        assertThat(bookTest.getPageNumber()).isEqualTo(1463);
        assertThat(bookTest.isActive()).isTrue();
    }
}
