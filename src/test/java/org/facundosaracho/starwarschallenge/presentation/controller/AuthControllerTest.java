package org.facundosaracho.starwarschallenge.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.facundosaracho.starwarschallenge.config.security.CustomUserDetailsService;
import org.facundosaracho.starwarschallenge.config.security.JwtService;
import org.facundosaracho.starwarschallenge.model.dto.JwtResponseDto;
import org.facundosaracho.starwarschallenge.model.dto.LoginRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();
    }


    @Test
    @DisplayName("Caso de éxito - AuthController - login con credenciales válidas")
    void login_ValidCredentials_ReturnsJwtToken() {
        // given
        String username = "admin";
        String password = "password123";
        String expectedToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...";

        LoginRequestDto loginRequest = new LoginRequestDto(username, password);

        doNothing().when(userDetailsService).validateCredentials(username, password);
        when(jwtService.generateToken(username)).thenReturn(expectedToken);

        // when
        ResponseEntity<JwtResponseDto> response = authController.login(loginRequest);

        // then
        verify(userDetailsService, times(1)).validateCredentials(username, password);
        verify(jwtService, times(1)).generateToken(username);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(expectedToken, response.getBody().token());
    }

    @Test
    @DisplayName("Caso de error - AuthController - login con credenciales inválidas")
    void login_InvalidCredentials_ThrowsException() {
        // given
        String username = "admin";
        String password = "wrongpassword";

        LoginRequestDto loginRequest = new LoginRequestDto(username, password);


        doThrow(new BadCredentialsException("Invalid credentials"))
                .when(userDetailsService).validateCredentials(username, password);

        // when & then
        assertThrows(BadCredentialsException.class, () -> {
            authController.login(loginRequest);
        });

        verify(userDetailsService, times(1)).validateCredentials(username, password);
        verify(jwtService, never()).generateToken(anyString());
    }

    @Test
    @DisplayName("Caso de error - AuthController - login con username null")
    void login_NullUsername_ThrowsException() {
        // given
        LoginRequestDto loginRequest = new LoginRequestDto(null, "password123");

        doThrow(new IllegalArgumentException("Username cannot be null"))
                .when(userDetailsService).validateCredentials(null, "password123");

        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
            authController.login(loginRequest);
        });

        verify(userDetailsService, times(1)).validateCredentials(null, "password123");
        verify(jwtService, never()).generateToken(anyString());
    }

    /*
    ---------------------------------------------------------------------------------------------------------------
    TESTS DE INTEGRACIÓN CON MockMvc
    ---------------------------------------------------------------------------------------------------------------
    */

    @Test
    @DisplayName("Caso de éxito - AuthController - /auth/login mediante mvc")
    void login_ValidRequest_ReturnsOk() throws Exception {
        // given
        String username = "admin";
        String password = "password123";
        String expectedToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...";

        LoginRequestDto loginRequest = new LoginRequestDto(username, password);

        doNothing().when(userDetailsService).validateCredentials(username, password);
        when(jwtService.generateToken(username)).thenReturn(expectedToken);

        // when & then
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token").value(expectedToken));

        verify(userDetailsService, times(1)).validateCredentials(username, password);
        verify(jwtService, times(1)).generateToken(username);
    }

    @Test
    @DisplayName("Caso de error - AuthController - /auth/login con JSON malformado")
    void login_MalformedJson_ReturnsBadRequest() throws Exception {
        // given
        String malformedJson = "{\"username\":\"admin\",\"password\":}";

        // when & then
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJson))
                .andExpect(status().isBadRequest());

        verify(userDetailsService, never()).validateCredentials(anyString(), anyString());
        verify(jwtService, never()).generateToken(anyString());
    }

    @Test
    @DisplayName("Caso de error - AuthController - /auth/login con campos faltantes")
    void login_MissingFields_ReturnsBadRequest() throws Exception {
        // given - Solo username, falta password
        LoginRequestDto incompleteRequest = new LoginRequestDto("admin", null);

        // when & then
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incompleteRequest)))
                .andExpect(status().isBadRequest());

        verify(userDetailsService, never()).validateCredentials(anyString(), anyString());
        verify(jwtService, never()).generateToken(anyString());
    }

    @Test
    @DisplayName("Caso de error - AuthController - /auth/login con username vacío")
    void login_EmptyUsername_ReturnsBadRequest() throws Exception {
        // given
        LoginRequestDto loginRequest = new LoginRequestDto("", "password123");

        // when & then
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());

        verify(userDetailsService, never()).validateCredentials(anyString(), anyString());
        verify(jwtService, never()).generateToken(anyString());
    }

    @Test
    @DisplayName("Caso de error - AuthController - /auth/login con password vacío")
    void login_EmptyPassword_ReturnsBadRequest() throws Exception {
        // given
        LoginRequestDto loginRequest = new LoginRequestDto("admin", "");

        // when & then
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());

        verify(userDetailsService, never()).validateCredentials(anyString(), anyString());
        verify(jwtService, never()).generateToken(anyString());
    }

    @Test
    @DisplayName("Caso de error - AuthController - /auth/login sin Content-Type")
    void login_MissingContentType_ReturnsUnsupportedMediaType() throws Exception {
        // given
        LoginRequestDto loginRequest = new LoginRequestDto("admin", "password123");

        // when & then
        mockMvc.perform(post("/auth/login")
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnsupportedMediaType());

        verify(userDetailsService, never()).validateCredentials(anyString(), anyString());
        verify(jwtService, never()).generateToken(anyString());
    }

    @Test
    @DisplayName("Caso borde - AuthController - login con caracteres especiales")
    void login_SpecialCharacters_HandledCorrectly() throws Exception {
        // given
        String username = "user@domain.com";
        String password = "P@ssw0rd!#$";

        LoginRequestDto loginRequest = new LoginRequestDto(username, password);

        String expectedToken = "token-with-special-chars";

        doNothing().when(userDetailsService).validateCredentials(username, password);
        when(jwtService.generateToken(username)).thenReturn(expectedToken);

        // when & then
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(expectedToken));

        verify(userDetailsService, times(1)).validateCredentials(username, password);
        verify(jwtService, times(1)).generateToken(username);
    }
}