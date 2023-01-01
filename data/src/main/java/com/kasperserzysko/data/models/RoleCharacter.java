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
public class RoleCharacter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "rolesLiked")
    private Set<User> roleLikes = new HashSet<>();

    @ManyToMany(mappedBy = "rolesDisliked")
    private Set<User> roleDislikes = new HashSet<>();

    @ManyToOne()
    private Person actor;

    @ManyToOne()
    private Movie movie;


    public void addActor(Person person){
        actor.getMoviesStarred().add(this);
        actor = person;
    }
    public void removeActor(){
        actor.getMoviesStarred().remove(this);
        actor = null;
        movie.getCharacters().remove(this);
        movie = null;
    }
}
