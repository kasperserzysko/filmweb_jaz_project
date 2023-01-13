package com.kasperserzysko.data.repositories;

import com.kasperserzysko.data.models.RoleCharacter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleCharacterRepository extends JpaRepository<RoleCharacter, Long> {


    @Query(nativeQuery = true, value = "SELECT rc.* FROM role_character rc\n" +
            "    inner join (SELECT rc.id, count(rc.id) as likes_count FROM role_character rc inner join user_roles_liked url on rc.id = url.role_id group by rc.name) likes on rc.id = likes.id\n" +
            "    inner join (SELECT rc.id, count(rc.id) as dislikes_count FROM role_character rc inner join user_roles_disliked urd on rc.id = urd.role_id group by rc.name) dislikes on rc.id = dislikes.id\n" +
            "    where rc.movie_id = :movieId\n" +
            "group by rc.id order by (likes.likes_count - dislikes.dislikes_count) desc")
    List<RoleCharacter> getMovieRoleCharactersByRating(Long movieId);

    @Query(nativeQuery = true, value = "SELECT rc.* FROM role_character rc\n" +
            "    inner join (SELECT rc.id, count(rc.id) as likes_count FROM role_character rc inner join user_roles_liked url on rc.id = url.role_id group by rc.name) likes on rc.id = likes.id\n" +
            "    inner join (SELECT rc.id, count(rc.id) as dislikes_count FROM role_character rc inner join user_roles_disliked urd on rc.id = urd.role_id group by rc.name) dislikes on rc.id = dislikes.id\n" +
            "    where rc.actor_id = :personId\n" +
            "group by rc.id order by (likes.likes_count - dislikes.dislikes_count) desc")
    List<RoleCharacter> getPersonRoleCharactersByRating(Long personId);

    @Query(nativeQuery = true, value = "SELECT rc.* FROM role rc inner join user_roles_liked url on rc.id = url.role_id WHERE url.user_id = :userId")
    List<RoleCharacter> getLikedRoleCharacters(Long userId, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT rc.* FROM role_character rc\n" +
            "    inner join (SELECT rc.id, count(rc.id) as likes_count FROM role_character rc inner join user_roles_liked url on rc.id = url.role_id group by rc.name) likes on rc.id = likes.id\n" +
            "    inner join (SELECT rc.id, count(rc.id) as dislikes_count FROM role_character rc inner join user_roles_disliked urd on rc.id = urd.role_id group by rc.name) dislikes on rc.id = dislikes.id\n" +
            "group by rc.id order by (likes.likes_count - dislikes.dislikes_count) desc LIMIT 100")
    List<RoleCharacter> getRoleCharactersByRating();

    @Query(nativeQuery = true, value = "SELECT COUNT(user_id) FROM user_roles_liked where role_id = :roleId")
    int getRoleLikes(Long roleId);

    @Query(nativeQuery = true, value = "SELECT COUNT(user_id) FROM user_roles_disliked where role_id = :roleId")
    int getRoleDislikes(Long roleId);
}
