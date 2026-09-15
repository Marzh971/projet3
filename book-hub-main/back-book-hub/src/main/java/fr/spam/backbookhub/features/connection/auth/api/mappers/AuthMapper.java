package fr.spam.backbookhub.features.connection.auth.api.mappers;

import fr.spam.backbookhub.features.connection.auth.api.dtos.AuthRegister;
import fr.spam.backbookhub.features.connection.user.models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    User toUser(AuthRegister authRegister);
}
