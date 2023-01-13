package com.kasperserzysko.web.services;

import com.kasperserzysko.data.models.Comment;
import com.kasperserzysko.data.models.Movie;
import com.kasperserzysko.data.models.RoleCharacter;
import com.kasperserzysko.data.repositories.DataRepository;
import com.kasperserzysko.web.cache.list_models.GenreList;
import com.kasperserzysko.web.cache.list_models.PersonList;
import com.kasperserzysko.web.cache.list_models.RoleCharacterList;
import com.kasperserzysko.web.dtos.*;
import com.kasperserzysko.web.exceptions.GenreNotFoundException;
import com.kasperserzysko.web.exceptions.MovieNotFoundException;
import com.kasperserzysko.web.exceptions.PersonNotFoundException;
import com.kasperserzysko.web.exceptions.UserNotFoundException;
import com.kasperserzysko.web.services.interfaces.IMovieService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
@Slf4j
public class MovieService implements IMovieService {

    private final DataRepository db;
    private final static int ITEMS_PER_PAGE = 10;

    public MovieService(DataRepository db) {
        this.db = db;
    }

    @Override
    public void addMovie(MovieDetailsDto movieDetailsDto) {
        log.info("Zaczynam Post requset");
        Movie movieEntity = new Movie();
        movieEntity.setTitle(movieDetailsDto.getDescription());
        movieEntity.setDescription(movieEntity.getDescription());
        movieEntity.setPremiere(movieDetailsDto.getPremiere());
        movieEntity.setLength(movieEntity.getLength());
        db.getMovies().save(movieEntity);
    }

    @Override
    public List<MovieDto> getMovies(Optional<String> keywordOptional, Optional<Integer> currentPageOptional) {
        Function<Movie, MovieDto> movieMapper = movie -> {
            var movieDto = new MovieDto();
            movieDto.setId(movie.getId());
            movieDto.setTitle(movie.getTitle());
            return movieDto;
        };
        int currentPage = currentPageOptional.orElse(1);

        Pageable pageable = PageRequest.of(currentPage - 1, ITEMS_PER_PAGE);

        if(keywordOptional.isEmpty()){
            return db.getMovies().getMovies(pageable).stream().map(movieMapper).toList();
        }
        return db.getMovies().getMoviesWithKeyword(keywordOptional.get(), pageable).stream().map(movieMapper).toList();
    }

    @Override
    @Cacheable(cacheNames = "cacheMovieDetails", key = "#movieId")
    public MovieDetailsDto getMovie(Long movieId) throws MovieNotFoundException {
        log.info("MOVIE FROM DB");
        var movieEntity = db.getMovies().findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Can't find movie with id: " + movieId));

        var movieDetailedDto = new MovieDetailsDto();
        movieDetailedDto.setTitle(movieEntity.getTitle());
        movieDetailedDto.setPremiere(movieEntity.getPremiere());
        movieDetailedDto.setLength(movieEntity.getLength());
        movieDetailedDto.setDescription(movieEntity.getDescription());
        return movieDetailedDto;
    }

    @Override
    @CachePut(cacheNames = "cacheMovieDetails", key = "#movieId")
    public MovieDetailsDto updateMovie(Long movieId, MovieDetailsDto movieDetailsDto) throws MovieNotFoundException {
        var movieEntity = db.getMovies().findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Can't find movie with id: " + movieId));

        movieEntity.setTitle(movieDetailsDto.getTitle());
        movieEntity.setPremiere(movieDetailsDto.getPremiere());
        movieEntity.setLength(movieDetailsDto.getLength());
        movieEntity.setDescription(movieDetailsDto.getDescription());
        db.getMovies().save(movieEntity);
        return movieDetailsDto;
    }

    @Override
    @CacheEvict(cacheNames = "cacheMovieDetails", key = "#movieId")                 //TODO OGARNIJ TO GOWNO
    public void deleteMovie(Long movieId) throws MovieNotFoundException {                           //Usuwam ręcznie film jak i wszystkie encje powiązane z nim, ponieważ usuwanie kaskadowe
        var movieEntity = db.getMovies().findById(movieId)                                   // nie zadziałało, kiedy niektóre z encji podrzędnych miały zostać zachowane.
                .orElseThrow(() -> new MovieNotFoundException("Can't find movie with id: " + movieId));

        movieEntity.getCharacters().forEach(roleCharacter -> {
            var personEntity = roleCharacter.getActor();
            movieEntity.removeCharacter(roleCharacter);
            db.getPeople().save(personEntity);
            db.getRoleCharacters().delete(roleCharacter);
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
            var userEntity = comment.getCommentCreator();
            movieEntity.removeComment(comment);
            db.getUsers().save(userEntity);
            db.getComments().delete(comment);
        });

        db.getMovies().deleteById(movieId);
    }

    @Override
    public void addMovieProducer(Long movieId, PersonIdDto dto) throws MovieNotFoundException, PersonNotFoundException {
        var movieEntity = db.getMovies().findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Can't find movie with id: " + movieId));
        var personEntity = db.getPeople().findById(dto.getPersonId())
                .orElseThrow(() -> new PersonNotFoundException("Can't find person with id: " + dto.getPersonId()));

        movieEntity.addProducer(personEntity);

        db.getMovies().save(movieEntity);
        db.getPeople().save(personEntity);
    }

    @Override
    @Cacheable(cacheNames = "cacheMovieProducerList", key = "#movieId")
    public PersonList getMovieProduces(Long movieId) throws MovieNotFoundException {
        db.getMovies().findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Can't find movie with id: " + movieId));

        List<PersonDto> personDtos =  db.getPeople().getMovieProducers(movieId).stream().map(person -> {
            var personDto = new PersonDto();
            personDto.setId(person.getId());
            personDto.setFirstName(person.getFirstName());
            personDto.setLastName(person.getLastName());
            return personDto;
        }).toList();

        return new PersonList(personDtos);
    }

    @Override
    public void addMovieRoleCharacter(Long movieId, RoleCharacterMovieDto dto) throws MovieNotFoundException, PersonNotFoundException {
        var movieEntity = db.getMovies().findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Can't find movie with id: " + movieId));
        var personEntity = db.getPeople().findById(dto.getPersonId())
                .orElseThrow(() -> new PersonNotFoundException("Can't find person with id: " + dto.getPersonId()));

        var roleCharacter = new RoleCharacter();

        roleCharacter.setName(dto.getName());

        movieEntity.addCharacter(roleCharacter);
        roleCharacter.addActor(personEntity);

        db.getRoleCharacters().save(roleCharacter);
        db.getMovies().save(movieEntity);
        db.getPeople().save(personEntity);
    }

    @Override
    @Cacheable(cacheNames = "cacheMovieRolesList", key = "#movieId")
    public RoleCharacterList getMovieRoles(Long movieId) throws MovieNotFoundException {
        log.info("Roles from DB");
        db.getMovies().findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Can't find movie with id: " + movieId));

        List<RoleCharacterDto> roleCharacterDtos = db.getRoleCharacters().getMovieRoleCharactersByRating(movieId).stream().map(roleCharacter -> {
            var roleCharacterDto = new RoleCharacterDto();
            roleCharacterDto.setId(roleCharacter.getId());
            roleCharacterDto.setName(roleCharacter.getName());
            return roleCharacterDto;
        }).toList();

        return new RoleCharacterList(roleCharacterDtos);
    }

    @Override
    public void addMovieGenre(Long movieId, GenreIdDto dto) throws MovieNotFoundException, GenreNotFoundException {
        var movieEntity = db.getMovies().findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Can't find movie with id: " + movieId));
        var genreEntity = db.getGenres().findById(dto.getGenreId()).orElseThrow(() -> new GenreNotFoundException("Can't find genre with id: " + dto.getGenreId()));

        movieEntity.addGenre(genreEntity);

        db.getMovies().save(movieEntity);
        db.getGenres().save(genreEntity);
    }

    @Override
    @Cacheable(cacheNames = "cacheMovieGenreList", key = "#movieId")
    public GenreList getMovieGenres(Long movieId) throws MovieNotFoundException {
        log.info("Genre from DB");
        db.getMovies().findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Can't find movie with id: " + movieId));
        List<GenreDto> genreDtos = db.getGenres().getGenreByMovieId(movieId).stream().map(genre -> {
            var genreDto = new GenreDto();
            genreDto.setId(genre.getId());
            genreDto.setName(genre.getName());
            return genreDto;
        }).toList();

        return new GenreList(genreDtos);
    }

    @Override
    public void likeMovie(Long movieId, SecurityUserDto user) throws MovieNotFoundException, UserNotFoundException {
        var loggedUser = user.getUser();
        var movieEntity = db.getMovies().findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Can't find movie with id: " + movieId));
        var loggedUserWithMoviesLiked = db.getUsers().getUserWithMoviesLiked(loggedUser.getId()).
                orElseThrow(() -> new UserNotFoundException("Couldn't find user with id: " + loggedUser.getId()));

        loggedUserWithMoviesLiked.likeMovie(movieEntity);

        db.getMovies().save(movieEntity);
        db.getUsers().save(loggedUserWithMoviesLiked);
    }

    @Override
    public int getMovieLikes(Long movieId) throws MovieNotFoundException {
        db.getMovies().findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Can't find movie with id: " + movieId));
        return db.getMovies().getMovieLikes(movieId);
    }

    @Override
    public void dislikeMovie(Long movieId, SecurityUserDto user) throws MovieNotFoundException, UserNotFoundException {
        var loggedUser = user.getUser();
        var movieEntity = db.getMovies().findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Can't find movie with id: " + movieId));
        var loggedUserWithMoviesDisliked = db.getUsers().getUserWithMoviesDisliked(loggedUser.getId()).
                orElseThrow(() -> new UserNotFoundException("Couldn't find user with id: " + loggedUser.getId()));

        loggedUserWithMoviesDisliked.dislikeMovie(movieEntity);

        db.getMovies().save(movieEntity);
        db.getUsers().save(loggedUserWithMoviesDisliked);
    }

    @Override
    public int getMovieDislikes(Long movieId) throws MovieNotFoundException {
        db.getMovies().findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Can't find movie with id: " + movieId));
        return db.getMovies().getMovieDislikes(movieId);
    }

    @Override
    public void addComment(Long movieId, CommentDto dto, SecurityUserDto user) throws MovieNotFoundException, UserNotFoundException {
        var movieEntity = db.getMovies().findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Can't find movie with id: " + movieId));

        var loggedUser = user.getUser();
        var loggedUserWithComments = db.getUsers().getUserWithComments(loggedUser.getId()).
                orElseThrow(() -> new UserNotFoundException("Couldn't find user with id: " + loggedUser.getId()));

        log.info(loggedUserWithComments.toString());
        var commentEntity = new Comment();

        commentEntity.setAddDate(LocalDateTime.now());
        commentEntity.setTitle(dto.getTitle());
        commentEntity.setContent(dto.getContent());

        loggedUserWithComments.addComment(commentEntity);
        movieEntity.addComment(commentEntity);

        db.getComments().save(commentEntity);
        db.getUsers().save(loggedUserWithComments);
        db.getMovies().save(movieEntity);
    }

    @Override
    public List<CommentDetailedDto> getComments(Long movieId, Optional<Integer> currentPageOptional) throws MovieNotFoundException {
        db.getMovies().findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Can't find movie with id: " + movieId));

        int currentPage = currentPageOptional.orElse(1);
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
    public List<MovieDto> getTopMovies(Optional<Integer> currentPageOptional) {
        int currentPage = currentPageOptional.orElse(1);
        Pageable pageable = PageRequest.of(currentPage - 1, ITEMS_PER_PAGE);

        return db.getMovies().getMoviesByRating(pageable).stream().map(movie -> {
            var movieDto = new MovieDto();
            movieDto.setId(movie.getId());
            movieDto.setTitle(movie.getTitle());
            return movieDto;
        }).toList();
    }

    @Override
    public void removeLike(Long movieId, SecurityUserDto userDto) throws MovieNotFoundException, UserNotFoundException {
        var movieEntity = db.getMovies().findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Can't find movie with id: " + movieId));

        var loggedUser = userDto.getUser();
        var loggedUserWithMoviesLiked = db.getUsers().getUserWithMoviesLiked(loggedUser.getId()).
                orElseThrow(() -> new UserNotFoundException("Couldn't find user with id: " + loggedUser.getId()));
        loggedUserWithMoviesLiked.removeMovieLike(movieEntity);

        db.getUsers().save(loggedUserWithMoviesLiked);
        db.getMovies().save(movieEntity);
    }

    @Override
    public void removeDislike(Long movieId, SecurityUserDto userDto) throws MovieNotFoundException, UserNotFoundException {
        var movieEntity = db.getMovies().findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Can't find movie with id: " + movieId));

        var loggedUser = userDto.getUser();
        var loggedUserWithMoviesDisliked = db.getUsers().getUserWithMoviesDisliked(loggedUser.getId()).
                orElseThrow(() -> new UserNotFoundException("Couldn't find user with id: " + loggedUser.getId()));

        loggedUserWithMoviesDisliked.removeMovieDislike(movieEntity);

        db.getUsers().save(loggedUserWithMoviesDisliked);
        db.getMovies().save(movieEntity);
    }


}
