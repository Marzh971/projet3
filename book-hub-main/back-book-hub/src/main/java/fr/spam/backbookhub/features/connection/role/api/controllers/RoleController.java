package fr.spam.backbookhub.features.connection.role.api.controllers;

import fr.spam.backbookhub.features.connection.role.models.Role;
import fr.spam.backbookhub.features.connection.role.services.IRoleService;
import fr.spam.backbookhub.shared.models.ServiceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
    private final IRoleService roleService;

    public RoleController(IRoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("")
    public ResponseEntity<ServiceResponse<List<Role>>> findAll() {
        return ResponseEntity
                .status(200)
                .body(roleService.findAll());
    }
}
