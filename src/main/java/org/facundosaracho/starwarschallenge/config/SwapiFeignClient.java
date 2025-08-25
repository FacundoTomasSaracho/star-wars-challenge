package org.facundosaracho.starwarschallenge.config;

import org.facundosaracho.starwarschallenge.model.dto.PaginatedPeopleResponseDto;
import org.facundosaracho.starwarschallenge.model.dto.SwapiPeopleByIdResponseDto;
import org.facundosaracho.starwarschallenge.model.dto.SwapiPeopleByNameResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "swapiClient", url = "${swapi.baseurl}")
public interface SwapiFeignClient {

    @GetMapping("/people/{id}")
    SwapiPeopleByIdResponseDto findPeopleById(@PathVariable("id") Long id);

    @GetMapping("/people")
    PaginatedPeopleResponseDto findAllPeople(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    );

    @GetMapping("/people")
    SwapiPeopleByNameResponseDto findPeopleByName(
            @RequestParam("name") String name
    );
}

