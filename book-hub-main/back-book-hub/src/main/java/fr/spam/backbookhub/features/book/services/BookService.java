package fr.spam.backbookhub.features.book.services;

import fr.spam.backbookhub.features.author.models.Author;
import fr.spam.backbookhub.features.author.repositories.AuthorJpaRepository;
import fr.spam.backbookhub.features.author.repositories.IAuthorRepository;
import fr.spam.backbookhub.features.book.api.dtos.BookListResponse;
import fr.spam.backbookhub.features.book.models.Book;
import fr.spam.backbookhub.features.book.repositories.IBookRepository;
import fr.spam.backbookhub.features.book.repositories.specifiactions.BookSpecification;
import fr.spam.backbookhub.features.category.models.Category;
import fr.spam.backbookhub.features.category.repositories.CategoryJpaRepository;
import fr.spam.backbookhub.features.category.repositories.ICategoryRepository;
import fr.spam.backbookhub.features.copy.models.Copy;
import fr.spam.backbookhub.features.copy.repositories.CopyJpaRepository;
import fr.spam.backbookhub.features.publisher.models.Publisher;
import fr.spam.backbookhub.features.publisher.repositories.IPublisherRepository;
import fr.spam.backbookhub.shared.models.ServiceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class BookService implements IBookService {
    public IBookRepository bookRepository;
    public IPublisherRepository publisherRepository;
    public AuthorJpaRepository authorRepository;
    public CategoryJpaRepository categoryRepository;
    public CopyJpaRepository copyRepository;

    public BookService(IBookRepository bookRepository, IPublisherRepository publisherRepository, AuthorJpaRepository authorRepository, CategoryJpaRepository categoryRepository, CopyJpaRepository copyRepository) {
        this.bookRepository = bookRepository;
        this.publisherRepository = publisherRepository;
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
        this.copyRepository = copyRepository;
    }

    @Override
    public ServiceResponse<Book> findByIsbn(String isbn) {
        try {
            return bookRepository.findById(isbn)
                    .map(book -> new ServiceResponse<>(
                            "200",
                            "Le livre a été trouvé",
                            book
                    ))
                    .orElseGet(() -> new ServiceResponse<>(
                            "404",
                            "Le livre n'a pas été trouvé",
                            null
                    ));
        } catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Une erreur est survenue lors de la récupération du livre par son isbn",
                    null
            );
        }
    }

    @Override
    public ServiceResponse<Book> findByTitle(String title) {
        return null;
    }

    @Override
    public ServiceResponse<List<Book>> findAll() {
        try {
            return new ServiceResponse<>(
                    "200",
                    "La liste des livres a été trouvée",
                    bookRepository.findAll()
            );
        } catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Une erreur est survenue lors de la récupération de la liste des livres",
                    null
            );
        }
    }

    @Override
    public ServiceResponse<List<Book>> findAllActive() {
        try {
            return new ServiceResponse<>(
                    "200",
                    "La liste des livres actifs a été trouvée",
                    bookRepository.findAllByIsActiveIsTrue()
            );
        } catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Une erreur est survenue lors de la récupération de la liste des livres",
                    null
            );
        }
    }

    @Override
    public ServiceResponse<Book> create(Book book) {

        if (bookRepository.findById(book.getIsbn()).isPresent()) {
            return new ServiceResponse<>(
                    "400",
                    "Le livre avec cet ISBN est déjà crée",
                    null
            );
        }
        try {
            if (book.getPublisher() != null && book.getPublisher().getId() != null) {
                Publisher publisher = publisherRepository.findById(book.getPublisher().getId())
                        .orElseThrow(() -> new RuntimeException("Éditeur introuvable"));
                book.setPublisher(publisher);
            } else {
                book.setPublisher(null);
            }

            if (book.getCategories() != null && !book.getCategories().isEmpty()) {
                List<Long> categoryIds = book.getCategories().stream()
                        .map(Category::getId)
                        .toList();
                Set<Category> categories = new HashSet<>(categoryRepository.findAllById(categoryIds));
                book.setCategories(categories);
            }

            if (book.getAuthors() != null && !book.getAuthors().isEmpty()) {
                List<Long> authorIds = book.getAuthors().stream()
                        .map(Author::getId)
                        .toList();
                Set<Author> authors = new HashSet<>(authorRepository.findAllById(authorIds));
                book.setAuthors(authors);
            }

            book.setActive(true);
            bookRepository.save(book);
            return new ServiceResponse<>(
                    "200",
                    "Le livre a été créé",
                    book
            );
        } catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Une erreur est survenue lors de la création du livre",
                    null
            );
        }
    }

    @Override
    public ServiceResponse<Book> createWithCopy(Book book, int copyNumber) {
        if (bookRepository.findById(book.getIsbn()).isPresent()) {
            return new ServiceResponse<>(
                    "400",
                    "Le livre avec cet ISBN est déjà crée",
                    null
            );
        }
        try {
            if (book.getPublisher() != null && book.getPublisher().getId() != null) {
                Publisher publisher = publisherRepository.findById(book.getPublisher().getId())
                        .orElseThrow(() -> new RuntimeException("Éditeur introuvable"));
                book.setPublisher(publisher);
            } else {
                book.setPublisher(null);
            }

            if (book.getCategories() != null && !book.getCategories().isEmpty()) {
                List<Long> categoryIds = book.getCategories().stream()
                        .map(Category::getId)
                        .toList();
                Set<Category> categories = new HashSet<>(categoryRepository.findAllById(categoryIds));
                book.setCategories(categories);
            }

            if (book.getAuthors() != null && !book.getAuthors().isEmpty()) {
                List<Long> authorIds = book.getAuthors().stream()
                        .map(Author::getId)
                        .toList();
                Set<Author> authors = new HashSet<>(authorRepository.findAllById(authorIds));
                book.setAuthors(authors);
            }

            book.setActive(true);
            Book savedBook = bookRepository.save(book);

            int nbCopies = copyNumber > 0 ? copyNumber : 1;
            List<Copy> copies = new ArrayList<>();
            for (int i = 0; i < nbCopies; i++) {
                Copy copy = Copy.builder()
                        .book(savedBook)
                        .barcode(generateBarcode())
                        .purchaseDate(LocalDateTime.now())
                        .build();
                copies.add(copy);
            }
            copyRepository.saveAll(copies);

            return new ServiceResponse<>(
                    "200",
                    "Le livre a été créé",
                    savedBook
            );
        } catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Une erreur est survenue lors de la création du livre",
                    null
            );
        }
    }

    @Override
    public ServiceResponse<Book> update(Book book) {
        try {
            Book existingBook = bookRepository.findById(book.getIsbn()).orElse(null);

            if (existingBook == null) {
                return new ServiceResponse<>(
                        "404",
                        "Le livre n'a pas été trouvé",
                        null
                );
            }

            existingBook.setTitle(book.getTitle());
            existingBook.setDescription(book.getDescription());
            existingBook.setCover(book.getCover());
            existingBook.setPublicationDate(book.getPublicationDate());
            existingBook.setPageNumber(book.getPageNumber());

            // Éditeur
            if (book.getPublisher() != null && book.getPublisher().getId() != null) {
                Publisher publisher = publisherRepository.findById(book.getPublisher().getId())
                        .orElseThrow(() -> new RuntimeException("Éditeur introuvable"));
                existingBook.setPublisher(publisher);
            } else {
                existingBook.setPublisher(null);
            }

            // Catégories
            if (book.getCategories() != null && !book.getCategories().isEmpty()) {
                List<Long> categoryIds = book.getCategories().stream()
                        .map(Category::getId)
                        .toList();
                Set<Category> categories = new HashSet<>(categoryRepository.findAllById(categoryIds));
                existingBook.setCategories(categories);
            } else {
                existingBook.setCategories(new HashSet<>());
            }

            // Auteurs
            if (book.getAuthors() != null && !book.getAuthors().isEmpty()) {
                List<Long> authorIds = book.getAuthors().stream()
                        .map(Author::getId)
                        .toList();
                Set<Author> authors = new HashSet<>(authorRepository.findAllById(authorIds));
                existingBook.setAuthors(authors);
            } else {
                existingBook.setAuthors(new HashSet<>());
            }

            Book savedBook = bookRepository.save(existingBook);

            return new ServiceResponse<>(
                    "200",
                    "Le livre a été mis à jour",
                    savedBook
            );
        } catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Erreur: " + e.getMessage(),
                    null
            );
        }
    }

    @Override
    public ServiceResponse<Void> delete(String isbn) {
        try {
            bookRepository.deleteById(isbn);
            return new ServiceResponse<>(
                    "200",
                    "Le livre a été supprimé",
                    null
            );
        } catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Une erreur est survenue lors de la suppression du livre",
                    null
            );
        }
    }

    @Override
    public ServiceResponse<Page<Book>> findAllFilter(String text, Long categoryId, Pageable pageable) {
        try {
            Specification<Book> specification = Specification
                    .where(BookSpecification.isActive());


            if (text != null && !text.isBlank()) {
                specification = specification.and(
                        BookSpecification.searchText(text)
                );
            }

            if (categoryId != null) {
                specification = specification.and(
                        BookSpecification.hasCategory(categoryId)
                );
            }

            Page<Book> booksPage = bookRepository.findAll(specification, pageable);

            return new ServiceResponse<>(
                    "200",
                    "La liste des livres a été trouvée",
                    booksPage
            );

        } catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Une erreur est survenue lors de la récupération des livres",
                    null
            );
        }
    }

    @Override
    public ServiceResponse<Void> setInactif(String isbn) {
        try {
            Book book = bookRepository.findById(isbn)
                    .orElseThrow(() -> new RuntimeException("Livre introuvable"));
            book.setActive(false);
            bookRepository.save(book);

            return new ServiceResponse<>(
                    "200",
                    "Le livre a été rendu inactif",
                    null
            );
        } catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Une erreur est survenue lors de la modification du status du livre",
                    null
            );
        }
    }

    private String generateBarcode() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
    }

}
