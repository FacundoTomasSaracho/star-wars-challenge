package org.facundosaracho.starwarschallenge.model.domain;

import java.util.List;

public record PeopleResponse(List<Result> results, String message, Long totalRecords, Long totalPages, String previous,
                             String next) {
}
