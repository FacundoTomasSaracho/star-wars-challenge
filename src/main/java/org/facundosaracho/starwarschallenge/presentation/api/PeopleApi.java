package org.facundosaracho.starwarschallenge.presentation.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.facundosaracho.starwarschallenge.presentation.dto.PaginatedPeopleDto;
import org.facundosaracho.starwarschallenge.presentation.dto.PeopleDTO;
import org.facundosaracho.starwarschallenge.presentation.exception.dto.ErrorDto;
import org.springframework.http.ResponseEntity;

@Tag(name = "People", description = "Operaciones relacionadas con personajes de Star Wars.")
public interface PeopleApi {

    @Operation(
            summary = "Buscar personaje por ID o nombre",
            description = "Permite buscar un personaje específico por ID o por nombre exacto. Se requiere al menos uno de los parámetros. Si" +
                    " busca por nombre, puede devolver una lista de personajes."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "At least one mandatory parameter is missing.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDto.class)
                    )),
            @ApiResponse(responseCode = "404", description = "People was not found with the provided parameter.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDto.class)
                    )),
            @ApiResponse(responseCode = "500", description = "Unexpected error.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDto.class)
                    )),
            @ApiResponse(responseCode = "500", description = "Unexpected error happened while trying to invoke star wars api.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDto.class)
                    ))
    })
    ResponseEntity<PeopleDTO> searchPeople(
            @Parameter(description = "ID específico del personaje", example = "1") Long id,
            @Parameter(description = "Nombre exacto del personaje", example = "Luke Skywalker") String name
    );

    @Operation(
            summary = "Obtener todos los personajes con paginación",
            description = "Devuelve una lista paginada de personajes de Star Wars. " +
                    "Se requiere indicar el tamaño de página y el número de página."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PaginatedPeopleDto.class)
                    )),
            @ApiResponse(responseCode = "400", description = "Parámetros de paginación inválidos (size o page ausentes o vacíos)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDto.class)
                    )),
            @ApiResponse(responseCode = "500", description = "Error inesperado al intentar recuperar los personajes",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDto.class)
                    ))
    })
    ResponseEntity<PaginatedPeopleDto> findAllPeople(
            @Parameter(description = "Número de elementos por página", example = "10") @Valid @NotBlank String size,
            @Parameter(description = "Número de página a recuperar", example = "1") @Valid @NotBlank String page
    );
}
