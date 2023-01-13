package com.kasperserzysko.web.cache.list_models;

import com.kasperserzysko.web.dtos.MovieDto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MovieList extends ArrayList<MovieDto> implements Serializable {

    private final List<MovieDto> movieDtos;

    public MovieList(List<MovieDto> movieDtos) {
        this.movieDtos = movieDtos;
    }

    public List<MovieDto> getMovieDtos() {
        return movieDtos;
    }
}
