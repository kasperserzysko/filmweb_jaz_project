package com.kasperserzysko.web.services;

import com.kasperserzysko.data.models.Comment;
import com.kasperserzysko.data.models.Movie;
import com.kasperserzysko.data.models.RoleCharacter;
import com.kasperserzysko.data.repositories.DataRepository;
import com.kasperserzysko.web.dtos.*;
import com.kasperserzysko.web.services.interfaces.IMovieService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

@Service
public class MovieService implements IMovieService {

    private final DataRepository db;
    private final static int ITEMS_PER_PAGE = 10;

    public MovieService(DataRepository db) {
        this.db = db;
    }

    @Override
    public void addMovie(MovieDetailsDto movieDetailsDto) {
        Movie movieEntity = new Movie();
        movieEntity.setTitle(movieDetailsDto.getDescription());
        movieEntity.setDescription(movieEntity.getDescription());
        movieEntity.setPremiere(movieDetailsDto.getPremiere());
        movieEntity.setLength(movieEntity.getLength());
        db.getMovies().save(movieEntity);
    }

    @Override
    public List<MovieDto> getMovies(String keyword, Integer currentPage) {
        Function<Movie, MovieDto> movieMapper = movie -> {
            var movieDto = new MovieDto();
            movieDto.setId(movie.getId());
            movieDto.setTitle(movie.getTitle());
            return movieDto;
        };

        if (currentPage == null){
            currentPage = 1;
        }
        Pageable pageable = PageRequest.of(currentPage - 1, ITEMS_PER_PAGE);

        if(keyword == null){
            return db.getMovies().getMovies(pageable).stream().map(movieMapper).toList();
        }
        return db.getMovies().getMoviesWithKeyword(keyword, pageable).stream().map(movieMapper).toList();
    }

    @Override
    public MovieDetailsDto getMovie(Long id) {
        var oMovieEntity = db.getMovies().findById(id);
        if (oMovieEntity.isPresent()){
            var movieEntity = oMovieEntity.get();
            var movieDetailedDto = new MovieDetailsDto();
            movieDetailedDto.setTitle(movieEntity.getTitle());
            movieDetailedDto.setPremiere(movieEntity.getPremiere());
            movieDetailedDto.setLength(movieEntity.getLength());
            movieDetailedDto.setDescription(movieEntity.getDescription());
            return movieDetailedDto;
        }
        return null;
    }

    @Override
    public void updateMovie(Long id, MovieDetailsDto movieDetailsDto) {
        var oMovieEntity = db.getMovies().findById(id);
        if (oMovieEntity.isPresent()) {
            var movieEntity = oMovieEntity.get();
            movieEntity.setTitle(movieDetailsDto.getTitle());
            movieEntity.setPremiere(movieDetailsDto.getPremiere());
            movieEntity.setLength(movieDetailsDto.getLength());
            movieEntity.setDescription(movieDetailsDto.getDescription());
            db.getMovies().save(movieEntity);
        }
    }

    @Override
    public void deleteMovie(Long id) {
        var oMovieEntity = db.getMovies().findById(id);
        if (oMovieEntity.isPresent()) {
            var movieEntity = oMovieEntity.get();

            movieEntity.getCharacters().forEach(roleCharacter -> {
                movieEntity.removeCharacter(roleCharacter);
                db.getRoleCharacters().save(roleCharacter);
            });
            movieEntity.getProducers().forEach(person -> {
                movieEntity.removeProducer(person);
                db.getPeople().save(person);
            });
            movieEntity.getGenres().forEach(genre -> {
                movieEntity.removeGenre(genre);
                db.getGenres().save(genre);
            });
            movieEntity.getComments().forEach(comment -> {
                movieEntity.removeComment(comment);
                db.getComments().save(comment);
            });

            db.getMovies().deleteById(id);
        }
    }

    @Override
    public void addMovieProducer(Long movieId, PersonIdDto dto) {
        var oMovieEntity = db.getMovies().findById(movieId);
        var oPersonEntity = db.getPeople().findById(dto.getPersonId());
        if (oMovieEntity.isPresent() && oPersonEntity.isPresent()) {
            var movieEntity = oMovieEntity.get();
            var personEntity = oPersonEntity.get();


            movieEntity.addProducer(personEntity);

            db.getMovies().save(movieEntity);
            db.getPeople().save(personEntity);
        }
    }

    @Override
    public List<PersonDto> getMovieProduces(Long movieId) {
        return db.getPeople().getMovieProducers(movieId).stream().map(person -> {
            var personDto = new PersonDto();
            personDto.setId(personDto.getId());
            personDto.setFirstName(person.getFirstName());
            personDto.setLastName(personDto.getLastName());
            return personDto;
        }).toList();
    }

    @Override
    public void addMovieRoleCharacter(Long movieId, RoleCharacterMovieDto dto) {
        var oMovieEntity = db.getMovies().findById(movieId);
        var oPersonEntity = db.getPeople().findById(dto.getPersonId());
        if (oMovieEntity.isPresent() && oPersonEntity.isPresent()) {
            var movieEntity = oMovieEntity.get();
            var personEntity = oPersonEntity.get();
            var roleCharacter = new RoleCharacter();

            roleCharacter.setName(dto.getName());

            movieEntity.addCharacter(roleCharacter);
            roleCharacter.addActor(personEntity);

            db.getRoleCharacters().save(roleCharacter);
            db.getMovies().save(movieEntity);
            db.getPeople().save(personEntity);

        }
    }

    @Override
    public List<RoleCharacterDto> getMovieRoles(Long movieId) {
        return db.getRoleCharacters().getRoleCharactersByRating(movieId).stream().map(roleCharacter -> {
            var roleCharacterDto = new RoleCharacterDto();
            roleCharacterDto.setId(roleCharacter.getId());
            roleCharacterDto.setName(roleCharacter.getName());
            return roleCharacterDto;
        }).toList();
    }

    @Override
    public void addMovieGenre(Long movieId, GenreIdDto dto) {
        var oMovieEntity = db.getMovies().findById(movieId);
        var oGenreEntity = db.getGenres().findById(dto.getGenreId());
        if (oMovieEntity.isPresent() && oGenreEntity.isPresent()) {
            var movieEntity = oMovieEntity.get();
            var genreEntity = oGenreEntity.get();

            movieEntity.addGenre(genreEntity);

            db.getMovies().save(movieEntity);
            db.getGenres().save(genreEntity);
        }
    }

    @Override
    public List<GenreDto> getMovieGenres(Long movieId) {
        return db.getGenres().getGenreByMovieId(movieId).stream().map(genre -> {
            var genreDto = new GenreDto();
            genreDto.setId(genre.getId());
            genreDto.setName(genre.getName());
            return genreDto;
        }).toList();
    }

    @Override
    public void likeMovie(Long movieId, SecurityUserDto user) {
        var loggedUser = user.getUser();
        var oMovieEntity = db.getMovies().findById(movieId);
        if (oMovieEntity.isPresent()) {
            var movieEntity = oMovieEntity.get();

            loggedUser.likeMovie(movieEntity);

            db.getMovies().save(movieEntity);
            db.getUsers().save(loggedUser);
        }
    }

    @Override
    public int getMovieLikes(Long movieId) {
        var oMovieEntity = db.getMovies().findById(movieId);
        int likes = 0;
        if (oMovieEntity.isPresent()) {
            var movieEntity = oMovieEntity.get();
            likes = movieEntity.getLikes().size();
        }
        return likes;
    }

    @Override
    public void dislikeMovie(Long movieId, SecurityUserDto user) {
        var loggedUser = user.getUser();
        var oMovieEntity = db.getMovies().findById(movieId);
        if (oMovieEntity.isPresent()) {
            var movieEntity = oMovieEntity.get();

            loggedUser.dislikeMovie(movieEntity);

            db.getMovies().save(movieEntity);
            db.getUsers().save(loggedUser);
        }
    }

    @Override
    public int getMovieDislikes(Long movieId) {
        var oMovieEntity = db.getMovies().findById(movieId);
        int disLikes = 0;
        if (oMovieEntity.isPresent()) {
            var movieEntity = oMovieEntity.get();
            disLikes = movieEntity.getDislikes().size();
        }
        return disLikes;
    }

    @Override
    public void removeLikeOrDislike(Long movieId, SecurityUserDto user) {
        var loggedUser = user.getUser();
        var oMovieEntity = db.getMovies().findById(movieId);
        if (oMovieEntity.isPresent()) {
            var movieEntity = oMovieEntity.get();

            loggedUser.removeLikeOrDislike(movieEntity);

            db.getMovies().save(movieEntity);
            db.getUsers().save(loggedUser);
        }
    }

    @Override
    public void addComment(Long movieId, CommentDto dto, SecurityUserDto user) {
        var oMovieEntity = db.getMovies().findById(movieId);
        var loggedUser = user.getUser();
        if (oMovieEntity.isPresent()) {
            var movieEntity = oMovieEntity.get();
            var commentEntity = new Comment();

            commentEntity.setAddDate(LocalDateTime.now());
            commentEntity.setTitle(dto.getTitle());
            commentEntity.setContent(dto.getContent());

            loggedUser.addComment(commentEntity);
            movieEntity.addComment(commentEntity);

            db.getComments().save(commentEntity);
            db.getUsers().save(loggedUser);
            db.getMovies().save(movieEntity);
        }
    }

    @Override
    public List<CommentDetailedDto> getComments(Long movieId, Integer currentPage) {
        if (currentPage == null){
            currentPage = 1;
        }
        Pageable pageable = PageRequest.of(currentPage - 1, ITEMS_PER_PAGE);

        return db.getComments().findAll(pageable).stream().map(comment -> {
            var commentDto = new CommentDetailedDto();
            commentDto.setId(comment.getId());
            commentDto.setTitle(comment.getTitle());
            commentDto.setContent(comment.getContent());
            commentDto.setAddDate(comment.getAddDate());
            return commentDto;
        }).toList();
    }

    @Override
    public List<MovieDto> getTopMovies(Integer currentPage) {
        if (currentPage == null){
            currentPage = 1;
        }
        Pageable pageable = PageRequest.of(currentPage - 1, ITEMS_PER_PAGE);

        return db.getMovies().getMoviesByRating(pageable).stream().map(movie -> {
            var movieDto = new MovieDto();
            movieDto.setId(movie.getId());
            movieDto.setTitle(movie.getTitle());
            return movieDto;
        }).toList();
    }


}
