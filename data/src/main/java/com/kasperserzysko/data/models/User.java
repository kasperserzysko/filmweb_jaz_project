package com.kasperserzysko.data.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;

    @OneToMany(mappedBy = "commentCreator")
    private List<Comment> comments = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "user_commets_liked",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "comment_id"))
    private List<Comment> commentsLiked = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "user_commets_disliked",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "comment_id"))
    private List<Comment> commentsDisliked = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "user_roles_liked",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<RoleCharacter> rolesLiked = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "user_roles_disliked",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<RoleCharacter> rolesDisliked = new ArrayList<>();


    @ManyToMany
    @JoinTable(
            name = "user_movies_liked",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id"))
    private List<Movie> moviesLiked = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "user_movies_disliked",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id"))
    private List<Movie> moviesDisliked = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles = new ArrayList<>();




    public void likeMovie(Movie movie){
        movie.getDislikes().remove(this);
        movie.getLikes().add(this);
        moviesLiked.add(movie);
    }
    public void removeMovieLike(Movie movie){
        movie.getLikes().remove(this);
        moviesLiked.remove(movie);
    }
    public void dislikeMovie(Movie movie){
        movie.getLikes().remove(this);
        movie.getDislikes().add(this);
        moviesDisliked.add(movie);
    }
    public void removeMovieDislike(Movie movie){
        movie.getDislikes().remove(this);
        moviesDisliked.remove(movie);
    }

    public void addComment(Comment comment){
        comment.setCommentCreator(this);
        comments.add(comment);
    }
    public void removeComment(Comment comment){
        comment.setCommentCreator(null);
        comments.remove(comment);
    }
    public void addCommentLike(Comment comment){
        comment.getUpVotes().add(this);
        commentsLiked.add(comment);
    }
    public void removeCommentLike(Comment comment){
        comment.getUpVotes().remove(this);
        commentsLiked.remove(comment);
    }
    public void addCommentDislike(Comment comment){
        comment.getDownVotes().add(this);
        commentsDisliked.add(comment);
    }
    public void removeCommentDislike(Comment comment){
        comment.getDownVotes().remove(this);
        commentsDisliked.remove(comment);
    }
    public void addRoleLike(RoleCharacter roleCharacter){
        roleCharacter.getRoleLikes().add(this);
        rolesLiked.add(roleCharacter);
    }
    public void removeRoleLike(RoleCharacter roleCharacter){
        roleCharacter.getRoleLikes().remove(this);
        rolesLiked.remove(roleCharacter);
    }
    public void addRoleDislike(RoleCharacter roleCharacter){
        roleCharacter.getRoleDislikes().add(this);
        rolesDisliked.add(roleCharacter);
    }
    public void removeRoleDislike(RoleCharacter roleCharacter){
        roleCharacter.getRoleDislikes().remove(this);
        rolesDisliked.remove(roleCharacter);
    }
    public void addRole(Role role){
        role.getUsers().add(this);
        roles.add(role);
    }
    public void removeRole(Role role){
        role.getUsers().remove(this);
        roles.remove(role);
    }
}
