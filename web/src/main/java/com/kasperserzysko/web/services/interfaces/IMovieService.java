package com.kasperserzysko.web.services.interfaces;

import com.kasperserzysko.web.dtos.*;
import com.kasperserzysko.web.exceptions.GenreNotFoundException;
import com.kasperserzysko.web.exceptions.MovieNotFoundException;
import com.kasperserzysko.web.exceptions.PersonNotFoundException;
import com.kasperserzysko.web.exceptions.UserNotFoundException;

import java.util.List;
import java.util.Optional;

public interface IMovieService {
    void addMovie(MovieDetailsDto movieDetailsDto);
    List<MovieDto> getMovies(Optional<String> keyword, Optional<Integer> currentPage);
    MovieDetailsDto getMovie(Long id) throws MovieNotFoundException;
    MovieDetailsDto updateMovie(Long id, MovieDetailsDto movieDetailsDto) throws MovieNotFoundException;
    void deleteMovie(Long id) throws MovieNotFoundException;
    void addMovieProducer(Long movieId, PersonIdDto dto) throws MovieNotFoundException, PersonNotFoundException;
    List<PersonDto> getMovieProduces(Long movieId) throws MovieNotFoundException;
    void addMovieRoleCharacter(Long movieId, RoleCharacterMovieDto dto) throws MovieNotFoundException, PersonNotFoundException;
    List<RoleCharacterDto> getMovieRoles(Long movieId) throws MovieNotFoundException;
    void addMovieGenre(Long movieId, GenreIdDto dto) throws MovieNotFoundException, GenreNotFoundException;
    List<GenreDto> getMovieGenres(Long movieId) throws MovieNotFoundException;
    void likeMovie(Long movieId, SecurityUserDto user) throws MovieNotFoundException, UserNotFoundException;
    int getMovieLikes(Long movieId) throws MovieNotFoundException;
    void dislikeMovie(Long movieId, SecurityUserDto user) throws MovieNotFoundException, UserNotFoundException;
    int getMovieDislikes(Long movieId) throws MovieNotFoundException;
    void addComment(Long movieId, CommentDto dto, SecurityUserDto userDto) throws MovieNotFoundException, UserNotFoundException;
    List<CommentDetailedDto> getComments(Long movieId, Optional<Integer> currentPage) throws MovieNotFoundException;
    List<MovieDto> getTopMovies(Optional<Integer> currentPage);
    void removeLike(Long movieId, SecurityUserDto userDto) throws MovieNotFoundException, UserNotFoundException;
    void removeDislike(Long movieId, SecurityUserDto userDto) throws MovieNotFoundException, UserNotFoundException;
}
