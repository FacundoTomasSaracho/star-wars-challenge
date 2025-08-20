package org.facundosaracho.starwarschallenge.model.domain;

import java.util.List;


public record PaginatedPeopleResponse(String message, Long total_records, Long total_pages,String next, String previous,
                                      List<Result> results) {

    public record Result(String uid, String name, String url) {
    }


}
