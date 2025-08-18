package org.facundosaracho.starwarschallenge.business.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PeopleResponse {

    private List<Result> results;
    private String message;
    private Long totalRecords;
    private Long totalPages;
    private String previous;
    private String next;

}
