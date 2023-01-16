package com.kasperserzysko.data.repositories;

import com.kasperserzysko.data.models.Movie;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query("SELECT m FROM Movie m WHERE m.title LIKE %:keyword% ORDER BY m.title")
    List<Movie> getMoviesWithKeyword(String keyword, Pageable pageable);

    @Query("SELECT m FROM Movie m ORDER BY m.title")
    List<Movie> getMovies(Pageable pageable);

    @Query(nativeQuery = true, value = "select m.* from movie m\n" +
            "    inner join (select m.id, count(m.id) as likes_count from movie m left join user_movies_liked uml on m.id = uml.movie_id group by m.id) likes on m.id = likes.id\n" +
            "    inner join (select m.id, count(m.id) as dislikes_count from movie m left join user_movies_disliked umd on m.id = umd.movie_id group by m.id) dislikes on m.id = dislikes.id\n" +
            "group by m.id order by (likes.likes_count - dislikes.dislikes_count) desc")
    List<Movie> getMoviesByRating(Pageable pageable);

    @Query(nativeQuery = true, value = "select m.* from movie m\n" +
            "    inner join movie_producers mp on m.id = mp.movie_id \n" +
            "    inner join (select m.id, count(m.id) as likes_count from movie m left join user_movies_liked uml on m.id = uml.movie_id WHERE mp.person_id = :personId group by m.id) likes on m.id = likes.id\n" +
            "    inner join (select m.id, count(m.id) as dislikes_count from movie m left join user_movies_disliked umd on m.id = umd.movie_id WHERE mp.person_id = :personId group by m.id) dislikes on m.id = dislikes.id\n" +
            "WHERE mp.person_id = :personId\n" +
            "group by m.id order by (likes.likes_count - dislikes.dislikes_count) desc;")
    List<Movie> getPersonMoviesByRating(Long personId);

    @Query(nativeQuery = true, value = "SELECT m.* FROM Movie m inner join user_movies_liked uml on m.id = uml.movie_id WHERE uml.user_id = :personId order by m.title")
    List<Movie> getLikedMovies(Long personId, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT COUNT(user_id) FROM user_movies_liked where movie_id = :movieId")
    int getMovieLikes(Long movieId);

    @Query(nativeQuery = true, value = "SELECT COUNT(user_id) FROM user_movies_disliked where movie_id = :movieId")
    int getMovieDislikes(Long movieId);

    @Query("SELECT m FROM Movie m LEFT JOIN FETCH m.characters WHERE m.id = :movieId ")
    Optional<Movie> findMovieByIdWithCharacters(Long movieId);
}
