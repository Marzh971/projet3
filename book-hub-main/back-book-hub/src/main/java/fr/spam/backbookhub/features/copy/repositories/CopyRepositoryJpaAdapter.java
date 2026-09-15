package fr.spam.backbookhub.features.copy.repositories;

import fr.spam.backbookhub.features.book.models.Book;
import fr.spam.backbookhub.features.copy.models.Copy;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CopyRepositoryJpaAdapter implements ICopyRepository {

    CopyJpaRepository copyJpaRepository;

    public CopyRepositoryJpaAdapter(CopyJpaRepository copyJpaRepository) {
        this.copyJpaRepository = copyJpaRepository;
    }

    @Override
    public List<Copy> findAll() {
        return copyJpaRepository.findAll();
    }

    @Override
    public List<Copy> findAllByBook(Book book) {
        return copyJpaRepository.findAllByBook(book);
    }

    @Override
    public Optional<Copy> findById(Long id) {
        return copyJpaRepository.findById(id);
    }

    @Override
    public Optional<Copy> findByBarcode(String barcode) {
        return copyJpaRepository.findByBarcode(barcode);
    }

    @Override
    public Copy save(Copy copy) {
        return copyJpaRepository.save(copy);
    }

    @Override
    public void deleteById(Long id) {
        copyJpaRepository.deleteById(id);
    }
}
