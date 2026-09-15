package fr.spam.backbookhub.features.book.api.controllers;

import fr.spam.backbookhub.features.book.api.dtos.BookListResponse;
import fr.spam.backbookhub.features.book.api.dtos.BookRequest;
import fr.spam.backbookhub.features.book.api.dtos.BookResponse;
import fr.spam.backbookhub.features.book.api.mappers.BookMapper;
import fr.spam.backbookhub.features.book.models.Book;
import fr.spam.backbookhub.features.book.services.IBookService;
import fr.spam.backbookhub.features.reservation.services.IReservationService;
import fr.spam.backbookhub.shared.models.ServiceResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/book")
public class BookController {
    private final IBookService bookService;
    private final BookMapper mapper;
    private final IReservationService reservationService;

    public BookController(IBookService bookService, BookMapper mapper, IReservationService reservationService) {
        this.bookService = bookService;
        this.mapper = mapper;
        this.reservationService = reservationService;
    }

    @GetMapping("/active")
    public ResponseEntity<ServiceResponse<List<BookListResponse>>> findAllActive() {
        return ResponseEntity.status(200).body(
                bookService.findAllActive()
                        .map(books -> books.stream().map(mapper::toBookListResponse).toList())
        );
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @GetMapping("")
    public ResponseEntity<ServiceResponse<List<BookListResponse>>> findAll() {
        return ResponseEntity.status(200).body(
                bookService.findAll()
                        .map(books -> books.stream().map(mapper::toBookListResponse).toList())
        );
    }

    @GetMapping("/{isbn}")
    public ResponseEntity<ServiceResponse<BookResponse>> findByIsbn(
            @Valid @PathVariable String isbn) {
        return ResponseEntity
                .status(200)
                .body(
                        bookService
                                .findByIsbn(isbn)
                                .map(mapper::toBookResponse)
                );
    }


    @GetMapping("/search")
    public ResponseEntity<ServiceResponse<Page<BookListResponse>>> search(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        ServiceResponse<Page<Book>> serviceResponse = bookService.findAllFilter(text, categoryId, pageable);

        // 3. Transformation de Page<Book> en Page<BookListResponse> tout en gardant les métadonnées de pagination
        Page<BookListResponse> bookListResponsePage = serviceResponse.getData() != null
                ? serviceResponse.getData().map(mapper::toBookListResponse)
                : Page.empty();

        // 4. Construction de la nouvelle réponse avec le bon type
        ServiceResponse<Page<BookListResponse>> finalResponse = new ServiceResponse<>(
                serviceResponse.getCode(), serviceResponse.getMessage(), bookListResponsePage
        );

        return ResponseEntity.ok(finalResponse);
    }



    @PreAuthorize("hasRole('LIBRARIAN')")
    @PostMapping("")
    public ResponseEntity<ServiceResponse<BookResponse>> create(
            @Valid @RequestBody BookRequest bookRequest) {

        return ResponseEntity
                .status(201)
                .body(
                        bookService
                                .create(mapper.toBook(bookRequest))
                                .map(mapper::toBookResponse)
                );
    }
    @PreAuthorize("hasRole('LIBRARIAN')")
    @PostMapping("/withCopy")
    public ResponseEntity<ServiceResponse<BookResponse>> createWithCopy(
            @Valid @RequestBody BookRequest bookRequest) {

        return ResponseEntity
                .status(201)
                .body(
                        bookService
                                .createWithCopy(mapper.toBook(bookRequest), bookRequest.copyNumber())
                                .map(mapper::toBookResponse)
                );
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @DeleteMapping("/{isbn}")
    public ResponseEntity<ServiceResponse<Void>> delete(@PathVariable String isbn) {
        ServiceResponse<Void> response = bookService.delete(isbn);

        return ResponseEntity
                .status(Integer.parseInt(response.getCode()))
                .body(response);

    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @PutMapping("/{isbn}/inactive")
    public ResponseEntity<ServiceResponse<Void>> setInactive(@PathVariable String isbn) {
        ServiceResponse<Void> response = bookService.setInactif(isbn);
        return ResponseEntity
                .status(Integer.parseInt(response.getCode()))
                .body(response);

    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @PutMapping("/{isbn}")
    public ResponseEntity<ServiceResponse<BookResponse>> update(@PathVariable String isbn,
            @Valid @RequestBody BookRequest bookRequest  ) {
        Book book = mapper.toBook(bookRequest);
        book.setIsbn(isbn);
        return ResponseEntity
                .status(200)
                .body(
                        bookService
                                .update(book)
                                .map(mapper::toBookResponse)
                );
    }

}



