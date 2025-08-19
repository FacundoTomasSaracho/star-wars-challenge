package org.facundosaracho.starwarschallenge.model.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Properties {
    private String created;
    private String edited;
    private String name;
    private String gender;
    private String skin_color;
    private String hair_color;
    private String height;
    private String eye_color;
    private String mass;
    private String homeworld;
    private String birth_year;
    private List<String> vehicles;
    private List<String> starships;
    private List<String> films;
    private String url;
}
