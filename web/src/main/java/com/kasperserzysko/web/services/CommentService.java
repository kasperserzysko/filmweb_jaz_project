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
    public void likeComment(Long commentId, SecurityUserDto securityUserDto) throws CommentNotFoundException {
        var loggedUser = securityUserDto.getUser();
        var commentEntity = db.getComments().findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Can't find comment with id: " + commentId));

        loggedUser.addCommentLike(commentEntity);

        db.getComments().save(commentEntity);
        db.getUsers().save(loggedUser);
    }

    @Override
    public int getCommentLikes(Long commentId) throws CommentNotFoundException {
        var commentEntity = db.getComments().findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Can't find comment with id: " + commentId));
        return commentEntity.getUpVotes().size();
    }

    @Override
    public void dislikeComment(Long commentId, SecurityUserDto securityUserDto) throws CommentNotFoundException {
        var loggedUser = securityUserDto.getUser();
        var commentEntity = db.getComments().findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Can't find comment with id: " + commentId));

        loggedUser.addCommentDislike(commentEntity);

        db.getComments().save(commentEntity);
        db.getUsers().save(loggedUser);
    }

    @Override
    public int getCommentDislikes(Long commentId) throws CommentNotFoundException {
        var commentEntity = db.getComments().findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Can't find comment with id: " + commentId));
        return commentEntity.getDownVotes().size();
    }

    @Override
    public void removeLikesAndDislikes(Long commentId, SecurityUserDto securityUserDto) throws CommentNotFoundException {
        var loggedUser = securityUserDto.getUser();
        var commentEntity = db.getComments().findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Can't find comment with id: " + commentId));

        loggedUser.removeCommentLikeOrDislike(commentEntity);

        db.getComments().save(commentEntity);
        db.getUsers().save(loggedUser);
    }
}
