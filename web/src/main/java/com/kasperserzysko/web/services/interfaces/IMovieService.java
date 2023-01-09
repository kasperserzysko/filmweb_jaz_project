package com.kasperserzysko.web.services.interfaces;

import com.kasperserzysko.web.dtos.*;
import com.kasperserzysko.web.exceptions.GenreNotFoundException;
import com.kasperserzysko.web.exceptions.MovieNotFoundException;
import com.kasperserzysko.web.exceptions.PersonNotFoundException;

import java.util.List;

public interface IMovieService {
    void addMovie(MovieDetailsDto movieDetailsDto);
    List<MovieDto> getMovies(String keyword, Integer currentPage);
    MovieDetailsDto getMovie(Long id) throws MovieNotFoundException;
    MovieDetailsDto updateMovie(Long id, MovieDetailsDto movieDetailsDto) throws MovieNotFoundException;
    void deleteMovie(Long id) throws MovieNotFoundException;
    void addMovieProducer(Long movieId, PersonIdDto dto) throws MovieNotFoundException, PersonNotFoundException;
    List<PersonDto> getMovieProduces(Long movieId) throws MovieNotFoundException;
    void addMovieRoleCharacter(Long movieId, RoleCharacterMovieDto dto) throws MovieNotFoundException, PersonNotFoundException;
    List<RoleCharacterDto> getMovieRoles(Long movieId) throws MovieNotFoundException;
    void addMovieGenre(Long movieId, GenreIdDto dto) throws MovieNotFoundException, GenreNotFoundException;
    List<GenreDto> getMovieGenres(Long movieId) throws MovieNotFoundException;
    void likeMovie(Long movieId, SecurityUserDto user) throws MovieNotFoundException;
    int getMovieLikes(Long movieId) throws MovieNotFoundException;
    void dislikeMovie(Long movieId, SecurityUserDto user) throws MovieNotFoundException;
    int getMovieDislikes(Long movieId) throws MovieNotFoundException;
    void addComment(Long movieId, CommentDto dto, SecurityUserDto userDto) throws MovieNotFoundException;
    List<CommentDetailedDto> getComments(Long movieId, Integer currentPage) throws MovieNotFoundException;
    List<MovieDto> getTopMovies(Integer currentPage);
    void removeLike(Long movieId, SecurityUserDto userDto) throws MovieNotFoundException;
    void removeDislike(Long movieId, SecurityUserDto userDto) throws MovieNotFoundException;
}
