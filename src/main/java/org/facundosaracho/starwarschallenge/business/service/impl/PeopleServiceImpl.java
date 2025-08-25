package org.facundosaracho.starwarschallenge.business.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.facundosaracho.starwarschallenge.business.service.PeopleService;
import org.facundosaracho.starwarschallenge.config.SwapiFeignClient;
import org.facundosaracho.starwarschallenge.exception.BusinessException;
import org.facundosaracho.starwarschallenge.mapper.PeopleMapper;
import org.facundosaracho.starwarschallenge.model.dto.PaginatedPeopleResponseDto;
import org.facundosaracho.starwarschallenge.model.domain.PeopleResponse;
import org.facundosaracho.starwarschallenge.model.dto.SwapiPeopleByIdResponseDto;
import org.facundosaracho.starwarschallenge.model.dto.SwapiPeopleByNameResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static org.facundosaracho.starwarschallenge.exception.dto.ErrorCodeDto.MANDATORY_PARAMETER_IS_MISSING;

@Service
@Slf4j
@RequiredArgsConstructor
public class PeopleServiceImpl implements PeopleService {

    private final SwapiFeignClient swapiClient;

    public PeopleResponse findPeopleByIdOrName(Long id, String name) {

        validateBothIdOrNameNotNull(id, name);

        if (id != null) {
            log.info("Realizando búsqueda de personaje por ID: {}", id);
            SwapiPeopleByIdResponseDto swapiResponse = swapiClient.findPeopleById(id);
            return PeopleMapper.INSTANCE.mapByIdResponseToPeopleResponse(swapiResponse);
        } else {
            log.info("Realizando búsqueda personaje por nombre: '{}'", name);
            SwapiPeopleByNameResponseDto swapiResponse = swapiClient.findPeopleByName(name);
            return PeopleMapper.INSTANCE.mapByNameResponseToPeopleResponse(swapiResponse);
        }
    }

    @Override
    public PeopleResponse findAllPeople(String page, String size) {
        PaginatedPeopleResponseDto paginatedPeopleResponseDto = swapiClient.findAllPeople(Integer.parseInt(page), Integer.parseInt(size));
        return PeopleMapper.INSTANCE.mapPaginatedPeopleResponseToPeopleResponse(paginatedPeopleResponseDto);
    }

    private static void validateBothIdOrNameNotNull(Long id, String name) {
        if (id == null && (name == null || name.isBlank())) {
            log.error("Se requiere al menos un parámetro de búsqueda (ID o nombre)");
            throw new BusinessException(MANDATORY_PARAMETER_IS_MISSING.getMessage(), MANDATORY_PARAMETER_IS_MISSING.getCode(), HttpStatus.BAD_REQUEST);
        }
    }
}
