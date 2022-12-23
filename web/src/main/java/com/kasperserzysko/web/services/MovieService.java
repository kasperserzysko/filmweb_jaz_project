package com.kasperserzysko.web.services;

import com.kasperserzysko.data.models.Movie;
import com.kasperserzysko.data.models.RoleCharacter;
import com.kasperserzysko.data.repositories.DataRepository;
import com.kasperserzysko.web.dtos.MovieDetailsDto;
import com.kasperserzysko.web.dtos.MovieDto;
import com.kasperserzysko.web.dtos.RoleCharacterDto;
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
            db.getMovies().deleteById(id);
        }
    }

    @Override
    public void addRoleCharacter(Long id, RoleCharacterDto roleCharacterDto) {
        var oMovieEntity = db.getMovies().findById(id);
        if (oMovieEntity.isPresent()) {
            var movieEntity = oMovieEntity.get();
            var roleCharacter = new RoleCharacter();
            //TODO
        }
    }


}
