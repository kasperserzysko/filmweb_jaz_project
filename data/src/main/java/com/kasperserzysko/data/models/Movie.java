package com.kasperserzysko.data.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
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

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(
            name = "movie_genres",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genres = new HashSet<>();

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(
            name = "movie_producers",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id"))
    private Set<Person> producers = new HashSet<>();

    @OneToMany()
    private Set<RoleCharacter> characters = new HashSet<>();

    @OneToMany
    private Set<Comment> comments = new HashSet<>();

    @ManyToMany
    private Set<User> likes = new HashSet<>();

    @ManyToMany
    private Set<User> dislikes = new HashSet<>();



    public void addCharacter(RoleCharacter roleCharacter){
        roleCharacter.setMovie(this);
        characters.add(roleCharacter);
    }
    public void removeCharacter(RoleCharacter roleCharacter){
        roleCharacter.setMovie(null);
        roleCharacter.removeActor();
        characters.remove(roleCharacter);
    }
    public void addProducer(Person person){
        person.getMoviesCreated().add(this);
        producers.add(person);
    }
    public void removeProducer(Person person){
        person.getMoviesCreated().remove(this);
        producers.remove(person);
    }

    public void addComment(Comment comment){
        comment.setMovie(this);
        comments.add(comment);
    }
    public void removeComment(Comment comment){
        comment.setMovie(null);
        comments.remove(comment);
    }

    public void addGenre(Genre genre){
        genre.getMovies().add(this);
        genres.add(genre);
    }
    public void removeGenre(Genre genre){
        genre.getMovies().remove(this);
        genres.remove(genre);
    }
}
