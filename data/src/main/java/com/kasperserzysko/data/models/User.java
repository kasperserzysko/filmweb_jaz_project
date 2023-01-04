package com.kasperserzysko.data.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
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

    @OneToMany
    private Set<Comment> comments = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "user_commetsLiked",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "comment_id"))
    private Set<Comment> commentsLiked = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "user_commetsDisliked",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "comment_id"))
    private Set<Comment> commentsDisliked = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "user_rolesLiked",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleCharacter> rolesLiked = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "user_rolesDisliked",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleCharacter> rolesDisliked = new HashSet<>();


    @ManyToMany
    @JoinTable(
            name = "user_moviesLiked",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id"))
    private Set<Movie> moviesLiked = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "user_rolesDisliked",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id"))
    private Set<Movie> moviesDisliked = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();




    public void likeMovie(Movie movie){
        movie.getDislikes().remove(this);
        movie.getLikes().add(this);
        moviesLiked.add(movie);
    }
    public void dislikeMovie(Movie movie){
        movie.getLikes().remove(this);
        movie.getDislikes().add(this);
        moviesDisliked.add(movie);
    }
    public void removeLikeOrDislike(Movie movie){
        movie.getDislikes().remove(this);
        movie.getLikes().remove(this);
        moviesDisliked.remove(movie);
        moviesLiked.remove(movie);
    }
    public void addComment(Comment comment){
        comment.setCommentCreator(this);
        comments.add(comment);
    }
    public void removeComment(Comment comment){
        comment.setCommentCreator(null);
        comments.remove(comment);
    }
    public void removeCommentLike(Comment comment){
        comment.getUpVotes().remove(this);
        commentsLiked.remove(comment);
    }
    public void removeCommentDislike(Comment comment){
        comment.getDownVotes().remove(this);
        commentsDisliked.remove(comment);
    }
    public void removeRoleLike(RoleCharacter roleCharacter){
        roleCharacter.getRoleLikes().remove(this);
        rolesLiked.remove(roleCharacter);
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
