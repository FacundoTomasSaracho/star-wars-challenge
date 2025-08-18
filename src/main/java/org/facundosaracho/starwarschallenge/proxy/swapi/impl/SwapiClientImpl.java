package org.facundosaracho.starwarschallenge.proxy.swapi.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.facundosaracho.starwarschallenge.business.model.PaginatedPeopleResponse;
import org.facundosaracho.starwarschallenge.business.model.SwapiPeopleByIdResponse;
import org.facundosaracho.starwarschallenge.business.model.SwapiPeopleByNameResponse;
import org.facundosaracho.starwarschallenge.exception.BusinessException;
import org.facundosaracho.starwarschallenge.exception.ClientException;
import org.facundosaracho.starwarschallenge.proxy.swapi.SwapiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.facundosaracho.starwarschallenge.exception.ErrorCode.SWAPI_CLIENT_ERROR;
import static org.facundosaracho.starwarschallenge.exception.ErrorCode.SWAPI_PEOPLE_NOT_FOUND;


@Slf4j
@Service
@RequiredArgsConstructor
public class SwapiClientImpl implements SwapiClient {

    private final RestTemplate restTemplate;

    @Value("${swapi.baseurl}")
    private String swapiBaseUrl;

    public SwapiPeopleByIdResponse findPeopleById(Long id) {

        String url = swapiBaseUrl + "/people/" + id + "/";
        log.info("Consultando SWAPI por ID en URL: {}", url);

        try {
            return restTemplate.getForObject(url, SwapiPeopleByIdResponse.class);
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
    public SwapiPeopleByNameResponse findPeopleByName(String name) {

        String url = swapiBaseUrl + "/people?name=" + name;
        log.info("Consultando SWAPI por nombre en url {}", url);

        try {
            return restTemplate.getForObject(url, SwapiPeopleByNameResponse.class);
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
        String url = swapiBaseUrl + "/people?page=" + page + "&limit=" + size;
        log.info("Obteniendo personajes desde: {}", url);
        try {
            return restTemplate.getForObject(url, PaginatedPeopleResponse.class);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ClientException(SWAPI_CLIENT_ERROR.getMessage(), SWAPI_CLIENT_ERROR.getCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
