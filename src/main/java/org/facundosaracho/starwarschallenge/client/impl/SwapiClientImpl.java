package org.facundosaracho.starwarschallenge.client.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.facundosaracho.starwarschallenge.client.SwapiClient;
import org.facundosaracho.starwarschallenge.config.SwapiFeignClient;
import org.facundosaracho.starwarschallenge.exception.BusinessException;
import org.facundosaracho.starwarschallenge.exception.ClientException;
import org.facundosaracho.starwarschallenge.model.dto.PaginatedPeopleResponseDto;
import org.facundosaracho.starwarschallenge.model.dto.SwapiPeopleByIdResponseDto;
import org.facundosaracho.starwarschallenge.model.dto.SwapiPeopleByNameResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import static org.facundosaracho.starwarschallenge.exception.dto.ErrorCodeDto.SWAPI_CLIENT_ERROR;
import static org.facundosaracho.starwarschallenge.exception.dto.ErrorCodeDto.SWAPI_PEOPLE_NOT_FOUND;


@Slf4j
@Service
@RequiredArgsConstructor
public class SwapiClientImpl implements SwapiClient {

    private final SwapiFeignClient swapiClient;

    public SwapiPeopleByIdResponseDto findPeopleById(Long id) {

        try {
            return swapiClient.findPeopleById(id);
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


    public SwapiPeopleByNameResponseDto findPeopleByName(String name) {

        try {
            return swapiClient.findPeopleByName(name);
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

    public PaginatedPeopleResponseDto findAllPeople(int size, int page) {
        try {
            return swapiClient.findAllPeople(size, page);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ClientException(SWAPI_CLIENT_ERROR.getMessage(), SWAPI_CLIENT_ERROR.getCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
