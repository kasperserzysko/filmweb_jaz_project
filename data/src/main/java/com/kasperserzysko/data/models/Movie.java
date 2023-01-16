package com.kasperserzysko.data.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    @Column(length = 1000)
    private String description;
    private LocalDate premiere;
    private int length;

    @ManyToMany
    @JoinTable(
            name = "movie_genres",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private List<Genre> genres = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "movie_producers",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id"))
    private List<Person> producers = new ArrayList<>();

    @OneToMany(mappedBy = "movie")
    private List<RoleCharacter> characters = new ArrayList<>();

    @OneToMany(mappedBy = "movie")
    private List<Comment> comments = new ArrayList<>();

    @ManyToMany(mappedBy = "moviesLiked")
    private List<User> likes = new ArrayList<>();

    @ManyToMany(mappedBy = "moviesDisliked")
    private List<User> dislikes = new ArrayList<>();



    public void addCharacter(RoleCharacter roleCharacter){
        roleCharacter.setMovie(this);
        characters.add(roleCharacter);
    }
    public void addProducer(Person person){
        person.getMoviesCreated().add(this);
        producers.add(person);
    }
    public void addComment(Comment comment){
        comment.setMovie(this);
        comments.add(comment);
    }
    public void addGenre(Genre genre){
        genre.getMovies().add(this);
        genres.add(genre);
    }

}
