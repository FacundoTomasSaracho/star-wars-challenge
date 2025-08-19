package org.facundosaracho.starwarschallenge.model.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PaginatedPeopleDto(
        List<PaginatedPersonDto> personDto,
        PaginationInformationDto pageInformation
) {

    public record PaginationInformationDto(String message, Long totalRecords, Long totalPages, String previous,
                                           String next) {
    }

    public record PaginatedPersonDto(String uid, String name, String url) {
    }
}
