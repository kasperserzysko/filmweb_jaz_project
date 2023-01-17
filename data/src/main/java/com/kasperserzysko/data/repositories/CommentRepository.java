package com.kasperserzysko.data.repositories;

import com.kasperserzysko.data.models.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {


    @Query(nativeQuery = true, value = "SELECT c.* FROM comment c where comment_creator_id = :userId order by c.add_date")
    List<Comment> getUserComments(Long userId, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT c.* FROM comment c INNER JOIN user_commets_liked ucl on c.id = ucl.comment_id where ucl.user_id = :userId")
    List<Comment> getLikedComments(Long userId, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT COUNT(user_id) FROM user_commets_liked where comment_id = :commentId")
    int getCommentLikes(Long commentId);

    @Query(nativeQuery = true, value = "SELECT COUNT(user_id) FROM user_commets_disliked where comment_id = :commentId")
    int getCommentDislikes(Long commentId);

    @Query("SELECT c From Comment c WHERE c.movie.id = :movieId")
    List<Comment> getMovieComments(Pageable pageable, Long movieId);
}
