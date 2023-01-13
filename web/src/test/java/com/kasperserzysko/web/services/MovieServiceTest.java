package com.kasperserzysko.web.services;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kasperserzysko.data.models.Movie;
import com.kasperserzysko.data.repositories.DataRepository;
import com.kasperserzysko.data.repositories.MovieRepository;
import com.kasperserzysko.web.dtos.MovieDetailsDto;
import com.kasperserzysko.web.exceptions.MovieNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

import javax.cache.CacheManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @InjectMocks
    private MovieService movieService;

    @Mock
    private DataRepository db;

    @Mock
    private MovieRepository movieRepository;

    @BeforeEach
    void setUp(){
        Mockito.when(db.getMovies()).thenReturn(movieRepository);
    }


    @Test
    void should_returnMovieDetailsDto() throws MovieNotFoundException {
        final var movieReturned = new Movie();
        movieReturned.setTitle("title");
        movieReturned.setDescription("description");
        movieReturned.setPremiere(null);
        movieReturned.setLength(100);


        final var expectedMovie = new MovieDetailsDto();
        expectedMovie.setTitle("title");
        expectedMovie.setDescription("description");
        expectedMovie.setPremiere(null);
        expectedMovie.setLength(100);

        Mockito.when(db.getMovies().findById(Mockito.anyLong())).thenReturn(Optional.of(movieReturned));

        final var actual = movieService.getMovie(2L);

        assertEquals(expectedMovie.getTitle(), actual.getTitle());
        assertEquals(expectedMovie.getDescription(), actual.getDescription());
        assertEquals(expectedMovie.getLength(), actual.getLength());
        assertEquals(expectedMovie.getPremiere(), actual.getPremiere());
    }

    @Test
    void should_returnMoviesNotFoundException(){
        Mockito.when(db.getMovies().findById(Mockito.anyLong())).thenReturn(Optional.empty());

        assertThrows(MovieNotFoundException.class, () -> movieService.getMovie(2L));
    }

    @Test
    void should_Return3Movies(){
        final var movie1 = new Movie();
        final var movie2 = new Movie();
        final var movie3 = new Movie();
        movie1.setTitle("title1");
        movie2.setTitle("title2");
        movie3.setTitle("title3");
        Pageable pageable = PageRequest.of(0, 10);

        List<Movie> moviesReturned = List.of(movie1, movie2, movie3);

        Mockito.when(db.getMovies().getMovies(pageable)).thenReturn(moviesReturned);

        assertEquals(3, movieService.getMovies(null, null).size());
        assertEquals("title1", movieService.getMovies(null, null).get(0).getTitle());
    }





}