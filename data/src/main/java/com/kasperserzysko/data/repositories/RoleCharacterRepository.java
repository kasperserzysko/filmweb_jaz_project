package com.kasperserzysko.data.repositories;

import com.kasperserzysko.data.models.RoleCharacter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleCharacterRepository extends JpaRepository<RoleCharacter, Long> {


    @Query(nativeQuery = true, value = "SELECT rc.* FROM role_character rc inner join user_roles_liked url on rc.id = url.role_id where rc.movie_id = :movieId group by rc.name order by count(rc.id) desc LIMIT 50")
    List<RoleCharacter> getRoleCharactersByRating(Long movieId);

    @Query(nativeQuery = true, value = "SELECT rc.* FROM role_character rc inner join user_roles_liked url on rc.id = url.role_id where rc.actor_id = :personId group by rc.name order by count(rc.id) desc LIMIT 50")
    List<RoleCharacter> getPersonRoleCharactersByRating(Long personId);

    @Query(nativeQuery = true, value = "SELECT rc.* FROM role rc inner join user_roles_liked url on rc.id = url.role_id WHERE url.user_id = :userId")
    List<RoleCharacter> getLikedRoleCharacters(Long userId, Pageable pageable);
}
