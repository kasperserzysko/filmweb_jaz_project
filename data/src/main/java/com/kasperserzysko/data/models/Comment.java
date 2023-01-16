package com.kasperserzysko.data.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    @Column(length = 1000)
    private String content;

    @ManyToOne
    private User commentCreator;

    @ManyToOne
    private Movie movie;

    @ManyToMany(mappedBy = "commentsLiked")
    private List<User> upVotes = new ArrayList<>();

    @ManyToMany(mappedBy = "commentsDisliked")
    private List<User> downVotes = new ArrayList<>();


    public void removeVotes(Comment comment){
        for (int indexRole = comment.getUpVotes().size() - 1; indexRole >= 0; indexRole--) {
            var user = comment.getUpVotes().get(indexRole);
            user.getCommentsLiked().remove(comment);
            comment.getUpVotes().remove(user);
        }
        for (int indexRole = comment.getDownVotes().size() - 1; indexRole >= 0; indexRole--) {
            var user = comment.getDownVotes().get(indexRole);
            user.getCommentsDisliked().remove(comment);
            comment.getDownVotes().remove(user);
        }
    }

}
