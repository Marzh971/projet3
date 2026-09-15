package fr.spam.backbookhub.features.connection.jwt.services;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Set<String> PUBLIC_PATHS = Set.of("/api/auth/login", "/api/auth/register");

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return PUBLIC_PATHS.contains(request.getServletPath());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = extractTokenFormCookie(request);
        if (token != null && jwtService.isValid(token)){
            String email = jwtService.extractUsername(token);
            try {
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                System.out.println("User : " + userDetails.getUsername());
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authToken);
            } catch (UsernameNotFoundException e) {
                // Token signé mais dont le sujet ne correspond plus à aucun utilisateur
                // (compte supprimé, ou token émis par une version antérieure).
                // On laisse le contexte vide : la requête finira en 401, pas en 500.
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);

    }

    private String extractTokenFormCookie(HttpServletRequest request){
        if (request.getCookies() == null){
            return null;
        }
        return Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals("access_token"))
                .map(Cookie::getValue)
                .findFirst().orElse(null);
    }
}
