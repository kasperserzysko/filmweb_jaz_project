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
    Optional<User> findUserByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.email LIKE %:keyword% ORDER BY u.email")
    List<User> getUsers(String keyword, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT u.* FROM user u INNER JOIN comment c ON c.comment_creator_id = u.id WHERE c.comment_creator_id = :userId")
    Optional<User> getCommentCreator(Long userId);
}