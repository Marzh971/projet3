package fr.spam.backbookhub.features.connection.jwt.services;

import fr.spam.backbookhub.features.connection.user.models.User;
import fr.spam.backbookhub.features.connection.user.repositories.IUserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final IUserRepository userRepository;

    public UserDetailsServiceImpl(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable."));
        return buidUserDetails(user);
    }


    private UserDetails buidUserDetails(User user) {
        return org.springframework.security.core.userdetails.User.
                withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(mapAuthorities(user))
                .build();
    }

    private Collection< ? extends GrantedAuthority> mapAuthorities(User user) {
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getLabel()))
                .toList();

    }
}
