package org.facundosaracho.starwarschallenge.model.dto;

import org.facundosaracho.starwarschallenge.model.domain.Result;

import java.util.List;

public record SwapiPeopleByNameResponseDto(List<Result> result, String message) {

}
