package org.facundosaracho.starwarschallenge.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.facundosaracho.starwarschallenge.model.domain.Result;

import java.util.List;

@Getter
@Setter
public class SwapiPeopleByNameResponseDto {

    private List<Result> result;
    private String message;
}
