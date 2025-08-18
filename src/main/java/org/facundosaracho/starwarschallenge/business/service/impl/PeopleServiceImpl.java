package org.facundosaracho.starwarschallenge.business.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.facundosaracho.starwarschallenge.business.model.PaginatedPeopleResponse;
import org.facundosaracho.starwarschallenge.business.model.PeopleResponse;
import org.facundosaracho.starwarschallenge.business.model.SwapiPeopleByIdResponse;
import org.facundosaracho.starwarschallenge.business.model.SwapiPeopleByNameResponse;
import org.facundosaracho.starwarschallenge.business.service.PeopleService;
import org.facundosaracho.starwarschallenge.exception.BusinessException;
import org.facundosaracho.starwarschallenge.mapper.PeopleResponseMapper;
import org.facundosaracho.starwarschallenge.proxy.swapi.SwapiClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static org.facundosaracho.starwarschallenge.exception.ErrorCode.MANDATORY_PARAMETER_IS_MISSING;

@Service
@Slf4j
@RequiredArgsConstructor
public class PeopleServiceImpl implements PeopleService {

    private final SwapiClient swapiClient;

    public PeopleResponse findPeopleByIdOrName(Long id, String name) {

        validateBothIdOrNameNotNull(id, name);

        if (id != null) {
            log.info("Realizando búsqueda de personaje por ID: {}", id);
            SwapiPeopleByIdResponse swapiResponse = swapiClient.findPeopleById(id);
            return PeopleResponseMapper.INSTANCE.mapByIdResponseToPeopleResponse(swapiResponse);
        } else {
            log.info("Realizando búsqueda personaje por nombre: '{}'", name);
            SwapiPeopleByNameResponse swapiResponse = swapiClient.findPeopleByName(name);
            return PeopleResponseMapper.INSTANCE.mapByNameResponseToPeopleResponse(swapiResponse);
        }
    }

    @Override
    public PeopleResponse findAllPeople(String page, String size) {
        PaginatedPeopleResponse paginatedPeopleResponse = swapiClient.findAllPeople(size, page);
        return PeopleResponseMapper.INSTANCE.mapPaginatedPeopleResponseToPeopleResponse(paginatedPeopleResponse);
    }

    private static void validateBothIdOrNameNotNull(Long id, String name) {
        if (id == null && (name == null || name.isBlank())) {
            log.error("Se requiere al menos un parámetro de búsqueda (ID o nombre)");
            throw new BusinessException(MANDATORY_PARAMETER_IS_MISSING.getMessage(), MANDATORY_PARAMETER_IS_MISSING.getCode(), HttpStatus.BAD_REQUEST);
        }
    }
}
