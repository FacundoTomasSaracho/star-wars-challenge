package org.facundosaracho.starwarschallenge.business.service;

import org.facundosaracho.starwarschallenge.business.model.PeopleResponse;

public interface PeopleService {

    PeopleResponse findPeopleByIdOrName(Long id, String name);
    PeopleResponse findAllPeople(String page, String size);

}
