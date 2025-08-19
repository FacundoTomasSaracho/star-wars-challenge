package org.facundosaracho.starwarschallenge.model.dto;

import java.util.List;

public record PeopleDTO(List<PersonDto> people,
                        String message,
                        int totalCount) {
}
