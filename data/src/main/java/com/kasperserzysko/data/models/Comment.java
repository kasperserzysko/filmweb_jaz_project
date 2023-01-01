package com.kasperserzysko.data.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime addDate;
    private String title;
    private String content;

    @ManyToOne
    private User commentCreator;

    @ManyToOne
    private Movie movie;

    @ManyToMany(mappedBy = "commentsLiked")
    private Set<User> upVotes = new HashSet<>();

    @ManyToMany(mappedBy = "commentsDisliked")
    private Set<User> downVotes = new HashSet<>();
}
