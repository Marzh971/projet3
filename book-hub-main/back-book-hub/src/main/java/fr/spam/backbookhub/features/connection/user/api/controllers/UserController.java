package fr.spam.backbookhub.features.connection.user.api.controllers;

import fr.spam.backbookhub.features.connection.user.models.User;
import fr.spam.backbookhub.features.connection.user.services.IUserService;
import fr.spam.backbookhub.features.connection.user.api.dtos.UserListResponse;
import fr.spam.backbookhub.features.connection.user.api.dtos.UserRequest;
import fr.spam.backbookhub.features.connection.user.api.dtos.UserResponse;
import fr.spam.backbookhub.features.connection.user.api.mappers.UserMapper;
import fr.spam.backbookhub.shared.models.ServiceResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private IUserService userService;
    private UserMapper mapper;

    public UserController(IUserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.mapper = userMapper;
    }

    @GetMapping("")
    public ResponseEntity<ServiceResponse<List<UserListResponse>>> findAll() {
        return ResponseEntity
                .status(200)
                .body(
                        userService
                                .findAll()
                                .map(users -> users
                                        .stream()
                                        .map(mapper::toUserListResponse)
                                        .toList())
                );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponse<UserResponse>> findById(
            @Valid @PathVariable UUID id) {
        return ResponseEntity
                .status(200)
                .body(
                        userService
                                .findById(id)
                                .map(mapper::toUserResponse)
                );
    }

    @PostMapping("")
    public ResponseEntity<ServiceResponse<UserResponse>> create(
            @Valid @RequestBody UserRequest userRequest) {
        return ResponseEntity
                .status(201)
                .body(
                        userService
                                .create(mapper.toUser(userRequest))
                                .map(mapper::toUserResponse)
                );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceResponse<UserResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UserRequest userRequest) {
        User user = mapper.toUser(userRequest);
        user.setId(id);

        ServiceResponse<User> response = userService.update(user);

        return ResponseEntity
                .status(Integer.parseInt(response.getCode()))
                .body(
                        response.map(mapper::toUserResponse)
                );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ServiceResponse<Void>> delete(@PathVariable UUID id) {
        ServiceResponse<Void> response = userService.delete(id);

        return ResponseEntity
                .status(Integer.parseInt(response.getCode()))
                .body(response);

    }
}