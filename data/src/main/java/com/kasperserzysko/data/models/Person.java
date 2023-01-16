package com.kasperserzysko.data.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    @Column(length = 1000)
    private String biography;
    private LocalDate birthday;
    private LocalDate deathday;

    @ManyToMany(mappedBy = "producers")
    private List<Movie> moviesCreated = new ArrayList<>();

    @OneToMany(mappedBy = "actor")
    private List<RoleCharacter> moviesStarred = new ArrayList<>();



}
