package org.facundosaracho.starwarschallenge.business.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SwapiPeopleByIdResponse {

    private Result result;
    private String message;

}


