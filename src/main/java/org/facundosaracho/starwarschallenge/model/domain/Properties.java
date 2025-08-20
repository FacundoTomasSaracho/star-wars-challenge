package org.facundosaracho.starwarschallenge.model.domain;

import java.util.List;

public record Properties(
        String created,
        String edited,
        String name,
        String gender,
        String skin_color,
        String hair_color,
        String height,
        String eye_color,
        String mass,
        String homeworld,
        String birth_year,
        List<String> vehicles,
        List<String> starships,
        List<String> films,
        String url
) {}
