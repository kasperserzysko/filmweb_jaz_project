package com.kasperserzysko.web.cache.list_wrappers;

import com.kasperserzysko.web.dtos.MovieDto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MovieListWrapper extends ArrayList<MovieDto> implements Serializable {

    private final List<MovieDto> movieDtos;

    public MovieListWrapper(List<MovieDto> movieDtos) {
        this.movieDtos = movieDtos;
    }

    public List<MovieDto> getMovieDtos() {
        return movieDtos;
    }
}
