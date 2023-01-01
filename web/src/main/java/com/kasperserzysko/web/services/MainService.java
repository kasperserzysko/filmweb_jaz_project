package com.kasperserzysko.web.services;

import com.kasperserzysko.web.services.interfaces.IRoleCharacterService;
import com.kasperserzysko.web.services.interfaces.IService;
import com.kasperserzysko.web.services.interfaces.IUserService;
import org.springframework.stereotype.Service;

@Service
public class MainService implements IService {

    private final MovieService movieService;
    private final PersonService personService;
    private final RoleCharacterService roleCharacterService;
    private final UserService userService;

    public MainService(MovieService movieService, PersonService personService, RoleCharacterService roleCharacterService, UserService userService) {
        this.movieService = movieService;
        this.personService = personService;
        this.roleCharacterService = roleCharacterService;
        this.userService = userService;
    }

    @Override
    public MovieService getMovies() {
        return movieService;
    }

    @Override
    public PersonService getPeople() {
        return personService;
    }

    @Override
    public IRoleCharacterService getRoleCharacters() {
        return roleCharacterService;
    }

    @Override
    public IUserService getUsers() {
        return userService;
    }
}
