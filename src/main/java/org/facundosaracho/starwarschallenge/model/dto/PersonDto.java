package org.facundosaracho.starwarschallenge.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PersonDto(String id,
                        String uid,
                        String description,
                        Integer version,
                        String name,
                        String gender,
                        String height,
                        String mass,
                        String hairColor,
                        String eyeColor,
                        String skinColor,
                        String birthYear,
                        String homeworld,
                        String created,
                        String edited,
                        String url,
                        List<String> vehicles,
                        List<String> starships,
                        List<String> films) {
}
