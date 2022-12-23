package com.kasperserzysko.data.repositories;

import org.springframework.stereotype.Repository;

@Repository
public class DataRepository implements IRepository {

    private final CommentRepository commentRepository;
    private final GenreRepository genreRepository;
    private final MovieRepository movieRepository;
    private final PersonRepository personRepository;
    private final RoleCharacterRepository roleCharacterRepository;
    private final UserRepository userRepository;

    public DataRepository(CommentRepository commentRepository, GenreRepository genreRepository, MovieRepository movieRepository, PersonRepository personRepository, RoleCharacterRepository roleCharacterRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.genreRepository = genreRepository;
        this.movieRepository = movieRepository;
        this.personRepository = personRepository;
        this.roleCharacterRepository = roleCharacterRepository;
        this.userRepository = userRepository;
    }

    @Override
    public CommentRepository getComments() {
        return commentRepository;
    }

    @Override
    public GenreRepository getGenres() {
        return genreRepository;
    }

    @Override
    public MovieRepository getMovies() {
        return movieRepository;
    }

    @Override
    public UserRepository getUsers() {
        return userRepository;
    }

    @Override
    public PersonRepository getPeople() {
        return personRepository;
    }

    @Override
    public RoleCharacterRepository getRoleCharacters() {
        return roleCharacterRepository;
    }
}
