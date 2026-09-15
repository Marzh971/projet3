package fr.spam.backbookhub.features.connection.user.api.mappers;


import fr.spam.backbookhub.features.connection.user.models.User;
import fr.spam.backbookhub.features.connection.user.api.dtos.UserListResponse;
import fr.spam.backbookhub.features.connection.user.api.dtos.UserRequest;
import fr.spam.backbookhub.features.connection.user.api.dtos.UserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserRequest userCreateRequest);

    UserResponse toUserResponse(User user);
    UserListResponse toUserListResponse(User user);
}
