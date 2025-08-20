package org.facundosaracho.starwarschallenge.client.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.facundosaracho.starwarschallenge.client.SwapiClient;
import org.facundosaracho.starwarschallenge.exception.BusinessException;
import org.facundosaracho.starwarschallenge.exception.ClientException;
import org.facundosaracho.starwarschallenge.model.domain.PaginatedPeopleResponse;
import org.facundosaracho.starwarschallenge.model.dto.SwapiPeopleByIdResponseDto;
import org.facundosaracho.starwarschallenge.model.dto.SwapiPeopleByNameResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import static org.facundosaracho.starwarschallenge.exception.dto.ErrorCodeDto.SWAPI_CLIENT_ERROR;
import static org.facundosaracho.starwarschallenge.exception.dto.ErrorCodeDto.SWAPI_PEOPLE_NOT_FOUND;


@Slf4j
@Service
@RequiredArgsConstructor
public class SwapiClientImpl implements SwapiClient {

    private final RestTemplate restTemplate;

    @Value("${swapi.baseurl}")
    private String swapiBaseUrl;

    public SwapiPeopleByIdResponseDto findPeopleById(Long id) {

        String url = UriComponentsBuilder.fromUriString(swapiBaseUrl)
                .path("/people/{id}/")
                .buildAndExpand(id)
                .toUriString();

        log.info("Consultando SWAPI por ID en URL: {}", url);

        try {
            return restTemplate.getForObject(url, SwapiPeopleByIdResponseDto.class);
        } catch (HttpClientErrorException e) {
            log.error("Personaje no encontrado en SWAPI: {}", id);
            throw new BusinessException(
                    SWAPI_PEOPLE_NOT_FOUND.getMessage(),
                    SWAPI_PEOPLE_NOT_FOUND.getCode(),
                    HttpStatus.NOT_FOUND
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ClientException(SWAPI_CLIENT_ERROR.getMessage(), SWAPI_CLIENT_ERROR.getCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public SwapiPeopleByNameResponseDto findPeopleByName(String name) {

        String url = UriComponentsBuilder.fromUriString(swapiBaseUrl)
                .path("/people")
                .queryParam("name", name)
                .build()
                .encode()
                .toUriString();

        log.info("Consultando SWAPI por nombre en url {}", url);

        try {
            return restTemplate.getForObject(url, SwapiPeopleByNameResponseDto.class);
        } catch (HttpClientErrorException e) {
            log.error("Personaje no encontrado en swapi: {}", name);
            throw new BusinessException(SWAPI_PEOPLE_NOT_FOUND.getMessage(),
                    SWAPI_PEOPLE_NOT_FOUND.getCode(),
                    HttpStatus.NOT_FOUND
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ClientException(SWAPI_CLIENT_ERROR.getMessage(), SWAPI_CLIENT_ERROR.getCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public PaginatedPeopleResponse findAllPeople(String size, String page) {
        String url = UriComponentsBuilder.fromUriString(swapiBaseUrl)
                .path("/people")
                .queryParam("page", page)
                .queryParam("limit", size)
                .build()
                .encode()
                .toUriString();

        log.info("Obteniendo personajes desde: {}", url);
        try {
            return restTemplate.getForObject(url, PaginatedPeopleResponse.class);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ClientException(SWAPI_CLIENT_ERROR.getMessage(), SWAPI_CLIENT_ERROR.getCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
