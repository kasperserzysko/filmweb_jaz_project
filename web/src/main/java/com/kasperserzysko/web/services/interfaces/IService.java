package com.kasperserzysko.web.services.interfaces;

import com.kasperserzysko.web.services.MovieService;
import com.kasperserzysko.web.services.PersonService;

public interface IService {

    MovieService getMovies();
    PersonService getPeople();
    IRoleCharacterService getRoleCharacters();
    IUserService getUsers();
}
