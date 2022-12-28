package com.kasperserzysko.data.repositories;

import com.kasperserzysko.data.models.Genre;
import com.kasperserzysko.data.models.RoleCharacter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

    //@Query("SELECT g FROM Genre g JOIN genre_movies gm ON g.id = gm.genre_id WHERE gm.movies_id = :movieId")
    @Query(nativeQuery = true, value = "SELECT g.* FROM genre g JOIN genre_movies gm on g.id = gm.genre_id WHERE gm.movies_id = :movieId")
    List<Genre> getGenreByMovieId(Long movieId);
    //SELECT id, name FROM genre g JOIN genre_movies gm on g.id = gm.genre_id WHERE gm.movies_id = 3;
}
