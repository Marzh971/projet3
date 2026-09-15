package fr.spam.backbookhub.features.book.repositories.specifiactions;

import fr.spam.backbookhub.features.book.models.Book;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {

    public static Specification<Book> isActive() {
        return (bookRoot, query, cb) ->
                cb.isTrue(bookRoot.get("isActive"));
    }

    public static Specification<Book> hasCategory(Long categoryId) {
        return (bookRoot, query, cb) ->
                cb.equal(
                        bookRoot.join("categories").get("id"),
                        categoryId
                );
    }

    public static Specification<Book> searchText(String value) {

        return (bookRoot, query, cb) -> {

            String keyword = "%" + value.toLowerCase() + "%";

            return cb.or(
                    cb.like(
                            cb.lower(bookRoot.get("title")),
                            keyword
                    ),
                    cb.like(
                            cb.lower(bookRoot.get("isbn")),
                            keyword
                    ),
                    cb.like(
                            cb.lower(bookRoot.join("authors", JoinType.LEFT)
                                    .get("lastName")),
                            keyword
                    ),
                    cb.like(
                            cb.lower(bookRoot.join("authors", JoinType.LEFT)
                                    .get("firstName")),
                            keyword
                    )
            );
        };
    }




}
