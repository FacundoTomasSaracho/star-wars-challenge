package org.facundosaracho.starwarschallenge.client;

import org.facundosaracho.starwarschallenge.model.dto.PaginatedPeopleResponseDto;
import org.facundosaracho.starwarschallenge.model.dto.SwapiPeopleByIdResponseDto;
import org.facundosaracho.starwarschallenge.model.dto.SwapiPeopleByNameResponseDto;

public interface SwapiClient {

    SwapiPeopleByIdResponseDto findPeopleById(Long id);
    SwapiPeopleByNameResponseDto findPeopleByName(String name);
    PaginatedPeopleResponseDto findAllPeople(int page, int limit);
}
