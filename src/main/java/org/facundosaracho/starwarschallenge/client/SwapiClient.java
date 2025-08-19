package org.facundosaracho.starwarschallenge.client;

import org.facundosaracho.starwarschallenge.model.domain.PaginatedPeopleResponse;
import org.facundosaracho.starwarschallenge.model.dto.SwapiPeopleByIdResponseDto;
import org.facundosaracho.starwarschallenge.model.dto.SwapiPeopleByNameResponseDto;

public interface SwapiClient {

    SwapiPeopleByIdResponseDto findPeopleById(Long id);

    SwapiPeopleByNameResponseDto findPeopleByName(String name);

    PaginatedPeopleResponse findAllPeople(String size, String page);

}
