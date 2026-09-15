package fr.spam.backbookhub.features.connection.auth.services;

import fr.spam.backbookhub.features.connection.auth.api.dtos.AuthLogin;
import fr.spam.backbookhub.features.connection.user.models.User;
import fr.spam.backbookhub.shared.models.ServiceResponse;

public interface IAuthService {
    ServiceResponse<Void> register(User user);
    ServiceResponse<Void> login(AuthLogin authLogin);
    ServiceResponse<Void> logout();
}
