package com.kasperserzysko.data.repositories;

import com.kasperserzysko.data.models.Movie;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query("SELECT m FROM Movie m WHERE m.title LIKE %:keyword% ORDER BY m.title")
    List<Movie> getMoviesWithKeyword(String keyword, Pageable pageable);

    @Query("SELECT m FROM Movie m ORDER BY m.title")
    List<Movie> getMovies(Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT m.* FROM movie m inner join user_movies_liked uml on m.id = uml.movie_id group by m.title order by count(m.id) desc LIMIT 50")
    List<Movie> getMoviesByRating(Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT m.* FROM movie m inner join user_movies_liked uml on m.id = uml.movie_id inner join movie_producers mp on m.id = mp.movie_id WHERE mp.person_id = :personId group by m.title order by count(m.id) desc ")
    List<Movie> getPersonMoviesByRating(Long personId);

    @Query(nativeQuery = true, value = "SELECT m.* FROM Movie m inner join user_movies_liked uml on m.id = uml.movie_id WHERE uml.user_id = :personId order by m.title")
    List<Movie> getLikedMovies(Long personId, Pageable pageable);
}
