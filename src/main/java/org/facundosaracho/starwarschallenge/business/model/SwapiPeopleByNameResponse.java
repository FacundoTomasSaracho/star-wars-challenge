package org.facundosaracho.starwarschallenge.business.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SwapiPeopleByNameResponse {

    private List<Result> result;
    private String message;
}
