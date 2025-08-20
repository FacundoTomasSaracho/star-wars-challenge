package org.facundosaracho.starwarschallenge.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.facundosaracho.starwarschallenge.model.domain.Result;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SwapiPeopleByIdResponseDto(Result result, String message) {
}


