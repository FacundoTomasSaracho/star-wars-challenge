package org.facundosaracho.starwarschallenge.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.facundosaracho.starwarschallenge.model.domain.Result;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SwapiPeopleByIdResponseDto {

    private Result result;
    private String message;

}


