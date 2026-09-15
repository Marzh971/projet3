package fr.spam.backbookhub.features.book.services;

import fr.spam.backbookhub.features.book.api.dtos.BookListResponse;
import fr.spam.backbookhub.features.book.models.Book;
import fr.spam.backbookhub.shared.models.ServiceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IBookService {
    /**
     * Retrouve un livre en le cherchant par son isbn.
     *
     * @param isbn l'identifiant unique du livre recherché.
     * @return un ServiceResponse contenant un objet Book. La réponse inclue des informations
     * tels que le code de statut, le message et le chargement utile de données comprenant le livre.
     */
    ServiceResponse<Book> findByIsbn(String isbn);

    /**
     * Retrouve un livre en le cherchant par son titre.
     *
     * @param title le titre du livre recherché.
     * @return un ServiceResponse contenant un objet Book. La réponse inclue des informations
     * tels que le code de statut, le message et le chargement utile de données comprenant le livre.
     */
    ServiceResponse<Book> findByTitle(String title);

    /**
     * Retrouve une liste contenant tous les livres.
     *
     * @return un ServiceResponse contenant une liste d'objets Book. La réponse inclue des informations
     * tels que le code de statut, le message et le chargement utile de données comprenant tous les livres.
     */
    ServiceResponse<List<Book>> findAll();

    /**
     * Retrouve une liste contenant tous les livres actifs.
     *
     * @return un ServiceResponse contenant une liste d'objets Book. La réponse inclue des informations
     * tels que le code de statut, le message et le chargement utile de données comprenant tous les livres actifs.
     */
    ServiceResponse<List<Book>> findAllActive();


    /**
     * Crée un nouveau livre.
     *
     * @param book le livre à créer.
     * @return un ServiceResponse contenant l'objet Book créé. La réponse inclue des informations
     * tels que le code de statut, le message et le chargement utile de données comprenant le livre.
     */
    ServiceResponse<Book> create(Book book  );

    /**
     * Crée un nouvel utilisateur.
     *
     * @param book le livre à créer.
     * @param copyNumber le nombre d'exemplaire
     * @return un ServiceResponse contenant l'objet Book créé. La réponse inclue des informations
     * tels que le code de statut, le message et le chargement utile de données comprenant le livre.
     */
    ServiceResponse<Book> createWithCopy(Book book , int copyNumber );

    /**
     * Met à jour un livre existant.
     *
     * @param book le livre à mettre à jour, identifié par son isbn.
     * @return un ServiceResponse contenant l'objet Book mis à jour. La réponse inclue des informations
     * tels que le code de statut, le message et le chargement utile de données comprenant le livre.
     */
    ServiceResponse<Book> update(Book book);





    /**
     * Supprime un livre en le cherchant par son isbn.
     *
     * @param isbn l'identifiant unique du livre à supprimer.
     * @return un ServiceResponse sans chargement utile de données. La réponse inclue des informations
     * tels que le code de statut et le message.
     */
    ServiceResponse<Void> delete(String isbn);


    /**
     * Retrouve une liste contenant tous les livres filtrés.
     *
     * @return un ServiceResponse contenant une liste d'objets Book. La réponse inclue des informations
     * tels que le code de statut, le message et le chargement utile de données comprenant tous les livres filtrés.
     */
    ServiceResponse<Page<Book>> findAllFilter(String text , Long categoryId, Pageable pageable);


    /**
     * Rend un livre inactif en le cherchant par son isbn.
     *
     * @param isbn l'identifiant unique du livre .
     * @return un ServiceResponse sans chargement utile de données. La réponse inclue des informations
     * tels que le code de statut et le message.
     */
    ServiceResponse<Void> setInactif(String isbn);

}
