package com.kasperserzysko.web.services;

import com.kasperserzysko.data.models.Movie;
import com.kasperserzysko.data.models.RoleCharacter;
import com.kasperserzysko.data.models.User;
import com.kasperserzysko.data.repositories.DataRepository;
import com.kasperserzysko.web.dtos.*;
import com.kasperserzysko.web.services.interfaces.IMovieService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService implements IMovieService {

    private final DataRepository db;

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
    public List<MovieDto> getMovies() {
        return db.getMovies().findAll().stream().map(movie -> {
            var movieDto = new MovieDto();
            movieDto.setId(movie.getId());
            movieDto.setTitle(movie.getTitle());
            return movieDto;
        }).toList();
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
                                                                                    //TODO PAMIETAJ O SEKCJI KOMENTARZY TUTAJ


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
        return db.getRoleCharacters().getRoleCharacterByMovieId(movieId).stream().map(roleCharacter -> {
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



}
