package org.facundosaracho.starwarschallenge.presentation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.facundosaracho.starwarschallenge.security.CustomUserDetailsService;
import org.facundosaracho.starwarschallenge.security.JwtService;
import org.facundosaracho.starwarschallenge.AuthControllerApi;
import org.facundosaracho.starwarschallenge.model.dto.JwtResponseDto;
import org.facundosaracho.starwarschallenge.model.dto.LoginRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController implements AuthControllerApi {

    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> login(@RequestBody @Valid LoginRequestDto loginRequest) {

        log.info("Intento de login para usuario: {}", loginRequest.getUsername());

        userDetailsService.validateCredentials(loginRequest.getUsername(), loginRequest.getPassword());

        String token = jwtUtil.generateToken(loginRequest.getUsername());

        log.info("Token generado exitosamente para usuario: {}", loginRequest.getUsername());

        return ResponseEntity.ok(new JwtResponseDto(token));

    }
}