package org.facundosaracho.starwarschallenge.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SwapiPeopleByNameResponseDto(List<Result> result, String message) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Result(
            Properties properties,
            @JsonProperty("_id") String id,
            String description,
            String uid,
            @JsonProperty("__v")Integer v,
            String url
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Properties(
            String created,
            String edited,
            String name,
            String gender,
            @JsonProperty("skin_color") String skinColor,
            @JsonProperty("hair_color")String hairColor,
            String height,
            @JsonProperty("eye_color")String eyeColor,
            String mass,
            String homeworld,
            @JsonProperty("birth_year")String birth_year,
            List<String> vehicles,
            List<String> starships,
            List<String> films,
            String url
    ) {
    }

}
