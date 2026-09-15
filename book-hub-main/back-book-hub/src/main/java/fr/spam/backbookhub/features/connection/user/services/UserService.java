package fr.spam.backbookhub.features.connection.user.services;

import fr.spam.backbookhub.features.connection.user.models.User;
import fr.spam.backbookhub.features.connection.user.repositories.IUserRepository;
import fr.spam.backbookhub.shared.models.ServiceResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService implements IUserService {

    private final IUserRepository userRepository;

    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ServiceResponse<User> findById(UUID id) {
        try {
            return new ServiceResponse<>(
                    "200",
                    "L'utilisateur a été trouvé",
                    userRepository.findById(id).orElse(null)
            );
        } catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Une erreur est survenue lors de la récupération de l'utilisateur par son id",
                    null
            );
        }
    }

    @Override
    public ServiceResponse<User> findByPseudo(String pseudo) {
        try {
            return new ServiceResponse<>(
                    "200",
                    "L'utilisateur a été trouvé",
                    userRepository.findByUsername(pseudo).orElse(null)
            );
        } catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Une erreur est survenue lors de la récupération de l'utilisateur par son pseudo",
                    null
            );
        }
    }

    @Override
    public ServiceResponse<User> findByEmail(String email) {
        try{
            return new ServiceResponse<>(
                    "200",
                    "L'utilisateur a été trouvé",
                    userRepository.findByEmail(email).orElse(null)
            );
        } catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Une erreur est survenue lors de la récupération de l'utilisateur par son email",
                    null
            );
        }
    }

    @Override
    public ServiceResponse<List<User>> findAll() {
        try {
            return new ServiceResponse<>(
                    "200",
                    "La liste des utilisateurs a été trouvée",
                    userRepository.findAll()
            );
        } catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Une erreur est survenue lors de la récupération de la liste des utilisateurs",
                    null
            );
        }
    }

    @Override
    public ServiceResponse<User> create(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()){
            return new ServiceResponse<>(
                    "400",
                    "Le pseudo est déjà utilisé",
                    null
            );
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()){
            return new ServiceResponse<>(
                    "400",
                    "L'email est déjà utilisé",
                    null
            );
        }
        if (userRepository.findByPhone(user.getPhone()).isPresent()){
            return new ServiceResponse<>(
                    "400",
                    "Le numéro de téléphone est déjà utilisé",
                    null
            );
        }
        try {
            userRepository.save(user);
            return new ServiceResponse<>(
                    "200",
                    "L'utilisateur a été créé",
                    user
            );
        } catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Une erreur est survenue lors de la création de l'utilisateur :" + e.getMessage(),
                    null
            );
        }
    }

    @Override
    public ServiceResponse<User> update(User user) {
        try {
            if (userRepository.findById(user.getId()).isPresent()) {
                userRepository.save(user);
                return new ServiceResponse<>(
                        "200",
                        "L'utilisateur a été mis à jour",
                        userRepository.findById(user.getId()).orElse(null)
                );
            }
            return new ServiceResponse<>(
                    "404",
                    "L'utilisateur n'a pas été trouvé",
                    null
            );
        } catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Une erreur est survenue lors de la mise à jour de l'utilisateur",
                    null
            );
        }
    }

    @Override
    public ServiceResponse<Void> delete(UUID id) {
        try {
            userRepository.deleteById(id);
            return new ServiceResponse<>(
                    "200",
                    "L'utilisateur a été supprimé",
                    null
            );
        } catch (Exception e) {
            return new ServiceResponse<>(
                    "500",
                    "Une erreur est survenue lors de la suppression de l'utilisateur",
                    null
            );
        }
    }
}
