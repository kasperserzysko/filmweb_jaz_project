package com.kasperserzysko.data.repositories;

import com.kasperserzysko.data.models.RoleCharacter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleCharacterRepository extends JpaRepository<RoleCharacter, Long> {

    @Query("SELECT r FROM RoleCharacter r WHERE r.movie.id = :movieId")
    List<RoleCharacter> getRoleCharacterByMovieId(Long movieId);
}
