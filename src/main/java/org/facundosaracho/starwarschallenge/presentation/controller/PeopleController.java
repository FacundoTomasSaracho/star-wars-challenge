package org.facundosaracho.starwarschallenge.presentation.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.facundosaracho.starwarschallenge.PeopleApi;
import org.facundosaracho.starwarschallenge.business.service.PeopleService;
import org.facundosaracho.starwarschallenge.mapper.PeopleResponseMapper;
import org.facundosaracho.starwarschallenge.model.domain.PeopleResponse;
import org.facundosaracho.starwarschallenge.model.dto.PaginatedPeopleDto;
import org.facundosaracho.starwarschallenge.model.dto.PeopleDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/people")
public class PeopleController implements PeopleApi {

    private final PeopleService peopleService;

    @GetMapping("/find")
    public ResponseEntity<PeopleDTO> findPeople(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name) {

        log.info("Empezando búsqueda de people por id o nombre en controller /search");
        PeopleResponse responseFromService = peopleService.findPeopleByIdOrName(id, name);
        PeopleDTO dto = PeopleResponseMapper.INSTANCE.mapPeopleResponseToPeopleDTO(responseFromService);
        log.info("Personaje/s encontrados satisfactoriamente.");
        return new ResponseEntity<>(dto, HttpStatus.OK);

    }

    @GetMapping("/find-all")
    public ResponseEntity<PaginatedPeopleDto> findAllPeople(
            @RequestParam @Valid @NotBlank String size,
            @RequestParam @Valid @NotBlank String page) {

        log.info("Empezando de búsqueda de people mediante paginación size: '{}', pages: '{}'", size, page);
        PeopleResponse peopleResponse = peopleService.findAllPeople(page, size);
        PaginatedPeopleDto dto = PeopleResponseMapper.INSTANCE.mapPeopleResponseToPaginatedPeopleDto(peopleResponse);
        log.info("Personajes encontrados satisfactoriamente.");
        return new ResponseEntity<>(dto, HttpStatus.OK);

    }
}
