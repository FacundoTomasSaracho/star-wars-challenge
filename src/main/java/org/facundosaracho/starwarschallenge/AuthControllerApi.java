package org.facundosaracho.starwarschallenge;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.facundosaracho.starwarschallenge.model.dto.JwtResponseDto;
import org.facundosaracho.starwarschallenge.model.dto.LoginRequestDto;
import org.facundosaracho.starwarschallenge.exception.dto.ErrorDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Auth", description = "Operaciones de autenticación y manejo de tokens JWT.")
public interface AuthControllerApi {

    @Operation(
            summary = "Autenticación de usuario",
            description = "Permite autenticar un usuario válido en el sistema enviando su nombre de usuario y contraseña. " +
                    "Si las credenciales son correctas, devuelve un **token JWT** que deberá utilizarse en las llamadas posteriores."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticación exitosa. Devuelve un token JWT válido.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JwtResponseDto.class)
                    )),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida. Credenciales mal formateadas o faltantes.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDto.class)
                    )),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas. Usuario o contraseña incorrectos.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDto.class)
                    )),
            @ApiResponse(responseCode = "500", description = "Error inesperado en el servidor.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDto.class)
                    ))
    })
    ResponseEntity<JwtResponseDto> login(
            @Valid @RequestBody
            LoginRequestDto loginRequest
    );
}
