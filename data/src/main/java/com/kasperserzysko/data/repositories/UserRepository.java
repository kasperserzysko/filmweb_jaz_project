package com.kasperserzysko.data.repositories;

import com.kasperserzysko.data.models.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findUserByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.email LIKE %:keyword% ORDER BY u.email")
    List<User> getUsers(String keyword, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT u.* FROM user u INNER JOIN comment c ON c.comment_creator_id = u.id WHERE c.id = :commentId")
    Optional<User> getCommentCreator(Long commentId);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.moviesLiked WHERE u.id = :userId")
    Optional<User> getUserWithMoviesLiked(Long userId);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.moviesDisliked WHERE u.id = :userId")
    Optional<User> getUserWithMoviesDisliked(Long userId);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.comments WHERE u.id = :userId")
    Optional<User> getUserWithComments(Long userId);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.commentsLiked WHERE u.id = :userId")
    Optional<User> getUserWithCommentsLiked(Long userId);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.commentsDisliked WHERE u.id = :userId")
    Optional<User> getUserWithCommentsDisliked(Long userId);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.rolesLiked WHERE u.id = :userId")
    Optional<User> getUserWithRolesLiked(Long userId);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.rolesDisliked WHERE u.id = :userId")
    Optional<User> getUserWithRolesDisliked(Long userId);
}