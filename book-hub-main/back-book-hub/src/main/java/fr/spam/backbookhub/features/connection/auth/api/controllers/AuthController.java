package fr.spam.backbookhub.features.connection.auth.api.controllers;

import fr.spam.backbookhub.features.connection.auth.api.dtos.AuthLogin;
import fr.spam.backbookhub.features.connection.auth.api.dtos.AuthRegister;
import fr.spam.backbookhub.features.connection.auth.api.dtos.CurrentUserDto;
import fr.spam.backbookhub.features.connection.auth.api.mappers.AuthMapper;
import fr.spam.backbookhub.features.connection.auth.services.IAuthService;
import fr.spam.backbookhub.features.connection.jwt.services.JwtService;
import fr.spam.backbookhub.features.connection.role.models.Role;
import fr.spam.backbookhub.features.connection.role.services.RoleService;
import fr.spam.backbookhub.features.connection.user.models.User;
import fr.spam.backbookhub.features.connection.user.services.IUserService;
import fr.spam.backbookhub.shared.models.ServiceResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final IAuthService authService;
    private final AuthMapper authMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final IUserService userService;

    public AuthController(IAuthService authService, AuthMapper authMapper, AuthenticationManager authenticationManager, JwtService jwtService, RoleService roleService, IUserService userService) {
        this.authService = authService;
        this.authMapper = authMapper;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<ServiceResponse<Void>> login(@RequestBody AuthLogin req, HttpServletResponse res) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.email(), req.password())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);

        ResponseCookie cookie = ResponseCookie.from("access_token", token)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(Duration.ofMinutes(30))
                .build();

        res.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        ServiceResponse<Void> response = this.authService.login(req);

        if (response.getCode().equals("200")) {
            return ResponseEntity
                    .status(200)
                    .body(response);
        }

        return ResponseEntity
                .status(400)
                .body(response);
    }

    @PostMapping("/register")
    public ResponseEntity<ServiceResponse<Void>> register(@RequestBody AuthRegister user) {

        ServiceResponse<Void> response = this.authService.register(authMapper.toUser(user));

        if (response.getCode().equals("204")) {
            return ResponseEntity
                    .status(204)
                    .body(response);
        }

        return ResponseEntity
                .status(404)
                .body(response);
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ServiceResponse<CurrentUserDto>> me(
            Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ServiceResponse<>("403", "Connection non authorisée", null));
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

         User user = userService.findByEmail(userDetails.getUsername()).getData();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ServiceResponse<>("403", "Connection non authorisée", null));
        }

        CurrentUserDto dto = new CurrentUserDto(user.getId(), user.getUsername(), roles);

        return ResponseEntity.ok(new ServiceResponse<>("200", "Succès de la connection", dto));
    }

    @DeleteMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ServiceResponse<Void>> logout(HttpServletResponse response) {
        ResponseCookie expiredCookie = ResponseCookie.from("access_token", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, expiredCookie.toString());

        return ResponseEntity.ok(new ServiceResponse<>("200", "Vous vous êtes déconnecté avec succès", null));
    }
}
