package com.kasperserzysko.web.services.interfaces;

import com.kasperserzysko.web.dtos.GenreDetailedDto;

public interface IGenreService {

    void addGenreMovie(Long movieId);
    void addGenre(GenreDetailedDto dto);
}
