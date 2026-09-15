package fr.spam.backbookhub.features.copy.services;

import fr.spam.backbookhub.features.book.models.Book;
import fr.spam.backbookhub.features.copy.models.Copy;
import fr.spam.backbookhub.features.copy.repositories.CopyJpaRepository;
import fr.spam.backbookhub.features.copy.repositories.ICopyRepository;
import fr.spam.backbookhub.shared.models.ServiceResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CopyService implements ICopyService{
    ICopyRepository copyRepository;
    CopyJpaRepository copyJpaRepository;

    public CopyService(ICopyRepository copyRepository, CopyJpaRepository copyJpaRepository) {
        this.copyRepository = copyRepository;
        this.copyJpaRepository = copyJpaRepository;
    }

    @Override
    public ServiceResponse<List<Copy>> findAll() {
        try {
            return new ServiceResponse<>(
                    "200",
                    "La liste des exemplaires a été récupérée avec succès.",
                    copyRepository.findAll()
            );
        }
        catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Une erreur est survenue lors de la récupération de la liste des exemplaires",
                    null
            );
        }
    }

    @Override
    public ServiceResponse<List<Copy>> findAllByBook(Book book) {
        try {
            return new ServiceResponse<>(
                    "200",
                    "La liste des exemplaires par livre a été récupérée avec succès.",
                    copyRepository.findAllByBook(book)
            );
        }
        catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Une erreur est survenue lors de la récupération de la liste des exemplaires par livre",
                    null
            );
        }
    }

    @Override
    public ServiceResponse<Copy> findById(Long id) {
        try {
            return new ServiceResponse<>(
                    "200",
                    "L'exemplaire a été récupéré avec succès.",
                    copyRepository.findById(id).orElseThrow()
            );
        }
        catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Une erreur est survenue lors de la récupération de l'exemplaire",
                    null
            );
        }
    }

    @Override
    public ServiceResponse<Copy> findByBarcode(String barcode) {
        try {
            return new ServiceResponse<>(
                    "200",
                    "L'exemplaire a été récupéré avec succès.",
                    copyRepository.findByBarcode(barcode).orElseThrow()
            );
        }
        catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Une erreur est survenue lors de la récupération de l'exemplaire",
                    null
            );
        }
    }

    @Override
    public ServiceResponse<Copy> create(Copy copy) {
        try {
            return new ServiceResponse<>(
                    "200",
                    "L'exemplaire a été créé avec succès.",
                    copyRepository.save(copy)
            );
        }
        catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Une erreur est survenue lors de la création de l'exemplaire",
                    null
            );
        }
    }

    @Override
    public ServiceResponse<Void> delete(Long id) {
        try {
            return new ServiceResponse<>(
                    "200",
                    "L'exemplaire a été supprimé avec succès.",
                    null
            );
        }
        catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Une erreur est survenue lors de la suppression de l'exemplaire",
                    null
            );
        }
    }

    @Override
    public ServiceResponse<Copy> findFirstAvailableCopy(String isbn) {
        try {
            return new ServiceResponse<>(
                    "200",
                    "L'exemplaire a été récupéré avec succès.",
                    copyJpaRepository.findAvailableCopiesByIsbn(isbn).stream().findFirst().orElseThrow()
            );
        }
        catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Une erreur est survenue lors de la récupération de l'exemplaire",
                    null
            );
        }
    }


}
