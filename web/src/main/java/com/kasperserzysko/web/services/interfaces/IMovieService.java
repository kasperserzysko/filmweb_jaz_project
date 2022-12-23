package com.kasperserzysko.web.services.interfaces;

import com.kasperserzysko.web.dtos.MovieDetailsDto;
import com.kasperserzysko.web.dtos.MovieDto;
import com.kasperserzysko.web.dtos.RoleCharacterDto;

import java.util.List;

public interface IMovieService {
    void addMovie(MovieDetailsDto movieDetailsDto);
    List<MovieDto> getMovies();
    MovieDetailsDto getMovie(Long id);
    void updateMovie(Long id, MovieDetailsDto movieDetailsDto);
    void deleteMovie(Long id);
    void addRoleCharacter(Long id, RoleCharacterDto roleCharacterDto);
}
