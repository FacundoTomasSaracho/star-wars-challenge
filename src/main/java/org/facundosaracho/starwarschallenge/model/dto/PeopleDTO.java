package org.facundosaracho.starwarschallenge.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PeopleDTO(List<PersonDto> people,
                        String message,
                        int totalCount) {
}
