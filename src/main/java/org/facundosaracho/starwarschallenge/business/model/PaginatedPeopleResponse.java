package org.facundosaracho.starwarschallenge.business.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PaginatedPeopleResponse {

    private String message;
    private Long total_records;
    private Long total_pages;
    private String previous;
    private String next;
    private List<Result> results;

    @Getter
    @Setter
    public static class Result {
        private String uid;
        private String name;
        private String url;
    }
}
