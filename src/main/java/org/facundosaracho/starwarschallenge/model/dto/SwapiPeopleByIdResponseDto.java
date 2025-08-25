package org.facundosaracho.starwarschallenge.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SwapiPeopleByIdResponseDto(
        String message,
        PeopleResult result
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PeopleResult(
            PeopleProperties properties,
            String description,
            @JsonProperty("_id") String id,
            String uid,
            @JsonProperty("__v") Integer v
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PeopleProperties(
            @JsonProperty("birth_year") String birthYear,
            @JsonProperty("eye_color") String eyeColor,
            @JsonProperty("hair_color") String hairColor,
            @JsonProperty("skin_color") String skinColor,
            String gender,
            String name,
            String height,
            String mass,
            String homeworld,
            List<String> vehicles,
            List<String> starships,
            List<String> films,
            String created,
            String edited,
            String url
    ) {
    }
}


