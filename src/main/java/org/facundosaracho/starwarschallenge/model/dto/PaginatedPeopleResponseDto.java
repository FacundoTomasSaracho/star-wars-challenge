package org.facundosaracho.starwarschallenge.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PaginatedPeopleResponseDto(String message,
                                         @JsonProperty("total_records") Long totalRecords,
                                         @JsonProperty("total_pages") Long totalPages,
                                         String next,
                                         String previous,
                                         List<Result> results) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Result(String uid, String name, String url) {
    }


}
