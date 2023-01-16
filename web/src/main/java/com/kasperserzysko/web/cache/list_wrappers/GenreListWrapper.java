package com.kasperserzysko.web.cache.list_wrappers;

import com.kasperserzysko.web.dtos.GenreDto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class GenreListWrapper extends ArrayList<GenreDto> implements Serializable {

    private final List<GenreDto> genreDtos;


    public GenreListWrapper(List<GenreDto> genreDtos) {
        this.genreDtos = genreDtos;
    }

    public List<GenreDto> getGenreDtos() {
        return genreDtos;
    }
}
