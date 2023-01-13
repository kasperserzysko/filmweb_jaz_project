package com.kasperserzysko.web.cache.list_models;

import com.kasperserzysko.web.dtos.GenreDto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class GenreList extends ArrayList<GenreDto> implements Serializable {

    private final List<GenreDto> genreDtos;


    public GenreList(List<GenreDto> genreDtos) {
        this.genreDtos = genreDtos;
    }

    public List<GenreDto> getGenreDtos() {
        return genreDtos;
    }
}
