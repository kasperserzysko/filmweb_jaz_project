package com.kasperserzysko.web.services.interfaces;

import com.kasperserzysko.data.models.User;
import com.kasperserzysko.web.dtos.*;

import java.util.List;

public interface IMovieService {
    void addMovie(MovieDetailsDto movieDetailsDto);
    List<MovieDto> getMovies();
    MovieDetailsDto getMovie(Long id);
    void updateMovie(Long id, MovieDetailsDto movieDetailsDto);
    void deleteMovie(Long id);
    void addMovieProducer(Long movieId, PersonIdDto dto);                 //TODO NIE ZAPOMNIJ O LISTOWANIU
    List<PersonDto> getMovieProduces(Long movieId);
    void addMovieRoleCharacter(Long movieId, RoleCharacterMovieDto dto);
    List<RoleCharacterDto> getMovieRoles(Long movieId);
    void addMovieGenre(Long movieId, GenreIdDto dto);
    List<GenreDto> getMovieGenres(Long movieId);
//    void likeMovie(Long movieId, User user);
//    int getMovieLikes(Long movieId);
//    void dislikeMovie(Long movie, User user);
//    int getMovieDislikes(Long movieId);
}
