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
    private Set<RoleCharacter> rolesLiked = new HashSet<>();

    @ManyToMany
    private Set<RoleCharacter> rolesDisliked = new HashSet<>();
}
