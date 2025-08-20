package org.facundosaracho.starwarschallenge.config.security;

import lombok.extern.slf4j.Slf4j;
import org.facundosaracho.starwarschallenge.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.facundosaracho.starwarschallenge.exception.dto.ErrorCodeDto.INVALID_CREDENTIALS;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    //todo agregar @Value

    private final PasswordEncoder passwordEncoder;
    private final Map<String, String> users = new HashMap<>();

    public CustomUserDetailsService(PasswordEncoder passwordEncoder,
                                    @Value("${app.users.admin.username:admin}") String adminUsername,
                                    @Value("${app.users.admin.password:admin123}") String adminPassword,
                                    @Value("${app.users.user.username:user}") String userUsername,
                                    @Value("${app.users.user.password:user123}") String userPassword) {
        this.passwordEncoder = passwordEncoder;

        // Cargar usuarios desde application.properties
        users.put(adminUsername, passwordEncoder.encode(adminPassword));
        users.put(userUsername, passwordEncoder.encode(userPassword));
    }

    // Spring Security lo usa para cargar datos del usuario
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String password = users.get(username);
        if (password == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }

        return new User(username, password, new ArrayList<>());
    }

    //Valida username/password durante el login
    public void validateCredentials(String username, String rawPassword) {
        String encodedPassword = users.get(username);
        if (encodedPassword == null || !passwordEncoder.matches(rawPassword, encodedPassword)) {
            log.warn("Credenciales inválidas.");
            throw new BusinessException(INVALID_CREDENTIALS.getMessage(), INVALID_CREDENTIALS.getCode(), HttpStatus.UNAUTHORIZED);
        }
    }
}
