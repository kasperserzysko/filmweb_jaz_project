package com.kasperserzysko.web.services;

import com.kasperserzysko.data.repositories.DataRepository;
import com.kasperserzysko.web.dtos.SecurityUserDto;
import com.kasperserzysko.web.dtos.UserDto;
import com.kasperserzysko.web.exceptions.CommentNotFoundException;
import com.kasperserzysko.web.exceptions.UserNotFoundException;
import com.kasperserzysko.web.services.interfaces.ICommentService;
import org.springframework.stereotype.Service;

@Service
public class CommentService implements ICommentService {

    private final DataRepository db;

    public CommentService(DataRepository db) {
        this.db = db;
    }

    @Override
    public UserDto getCommentCreator(Long commentId) throws UserNotFoundException {
        var userEntity = db.getUsers().getCommentCreator(commentId)
                .orElseThrow(() -> new UserNotFoundException("Couldn't find user of this comment"));

        var userDto = new UserDto();
        userDto.setId(userEntity.getId());
        userDto.setEmail(userEntity.getEmail());

        return userDto;
    }

    @Override
    public void likeComment(Long commentId, SecurityUserDto securityUserDto) throws CommentNotFoundException, UserNotFoundException {
        var loggedUser = securityUserDto.getUser();
        var commentEntity = db.getComments().findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Can't find comment with id: " + commentId));
        var loggedUserWithCommentsLiked = db.getUsers().getUserWithCommentsLiked(loggedUser.getId()).
                orElseThrow(() -> new UserNotFoundException("Couldn't find user with id: " + loggedUser.getId()));
        if (!commentEntity.getUpVotes().contains(loggedUserWithCommentsLiked)) {
            loggedUserWithCommentsLiked.addCommentLike(commentEntity);

            db.getComments().save(commentEntity);
            db.getUsers().save(loggedUserWithCommentsLiked);
        }
    }

    @Override
    public int getCommentLikes(Long commentId) throws CommentNotFoundException {
        db.getComments().findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Can't find comment with id: " + commentId));
        return db.getComments().getCommentLikes(commentId);
    }

    @Override
    public void dislikeComment(Long commentId, SecurityUserDto securityUserDto) throws CommentNotFoundException, UserNotFoundException {
        var loggedUser = securityUserDto.getUser();
        var commentEntity = db.getComments().findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Can't find comment with id: " + commentId));
        var loggedUserWithCommentsDisliked =  db.getUsers().getUserWithCommentsDisliked(loggedUser.getId()).
                orElseThrow(() -> new UserNotFoundException("Couldn't find user with id: " + loggedUser.getId()));
        if (!commentEntity.getDownVotes().contains(loggedUserWithCommentsDisliked)) {
            loggedUserWithCommentsDisliked.addCommentDislike(commentEntity);

            db.getComments().save(commentEntity);
            db.getUsers().save(loggedUserWithCommentsDisliked);
        }
    }

    @Override
    public int getCommentDislikes(Long commentId) throws CommentNotFoundException {
        db.getComments().findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Can't find comment with id: " + commentId));
        return db.getComments().getCommentDislikes(commentId);
    }


    @Override
    public void removeLike(Long commentId, SecurityUserDto securityUserDto) throws CommentNotFoundException, UserNotFoundException {
        var loggedUser = securityUserDto.getUser();
        var commentEntity = db.getComments().findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Can't find comment with id: " + commentId));
        var loggedUserWithCommentsLiked =  db.getUsers().getUserWithCommentsLiked(loggedUser.getId()).
                orElseThrow(() -> new UserNotFoundException("Couldn't find user with id: " + loggedUser.getId()));

        loggedUserWithCommentsLiked.removeCommentLike(commentEntity);

        db.getComments().save(commentEntity);
        db.getUsers().save(loggedUser);
    }

    @Override
    public void removeDislike(Long commentId, SecurityUserDto securityUserDto) throws CommentNotFoundException, UserNotFoundException {
        var loggedUser = securityUserDto.getUser();
        var commentEntity = db.getComments().findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Can't find comment with id: " + commentId));
        var loggedUserWithCommentsDisliked =  db.getUsers().getUserWithCommentsDisliked(loggedUser.getId()).
                orElseThrow(() -> new UserNotFoundException("Couldn't find user with id: " + loggedUser.getId()));

        loggedUserWithCommentsDisliked.removeCommentDislike(commentEntity);

        db.getComments().save(commentEntity);
        db.getUsers().save(loggedUser);
    }
}
