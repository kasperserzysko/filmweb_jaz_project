package com.kasperserzysko.data.repositories;

public interface IRepository {

    CommentRepository getComments();
    GenreRepository getGenres();
    MovieRepository getMovies();
    UserRepository getUsers();
    PersonRepository getPeople();
    RoleCharacterRepository getRoleCharacters();
}
