package org.facundosaracho.starwarschallenge.proxy.swapi;

import org.facundosaracho.starwarschallenge.business.model.PaginatedPeopleResponse;
import org.facundosaracho.starwarschallenge.business.model.SwapiPeopleByIdResponse;
import org.facundosaracho.starwarschallenge.business.model.SwapiPeopleByNameResponse;

public interface SwapiClient {

    SwapiPeopleByIdResponse findPeopleById(Long id);

    SwapiPeopleByNameResponse findPeopleByName(String name);

    PaginatedPeopleResponse findAllPeople(String size, String page);

}
