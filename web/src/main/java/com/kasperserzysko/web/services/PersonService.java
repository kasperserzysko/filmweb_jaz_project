package com.kasperserzysko.web.services;

import com.kasperserzysko.data.models.Person;
import com.kasperserzysko.data.repositories.DataRepository;
import com.kasperserzysko.web.cache.list_models.MovieList;
import com.kasperserzysko.web.cache.list_models.RoleCharacterList;
import com.kasperserzysko.web.dtos.MovieDto;
import com.kasperserzysko.web.dtos.PersonDetailedDto;
import com.kasperserzysko.web.dtos.PersonDto;
import com.kasperserzysko.web.dtos.RoleCharacterDto;
import com.kasperserzysko.web.exceptions.PersonNotFoundException;
import com.kasperserzysko.web.services.interfaces.IPersonService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public class PersonService implements IPersonService {

    private final DataRepository db;

    public PersonService(DataRepository db) {
        this.db = db;
    }



    @Override
    public void addPerson(PersonDetailedDto personDetailedDto) {
        var personEntity = new Person();
        personEntity.setFirstName(personEntity.getFirstName());
        personEntity.setLastName(personEntity.getLastName());
        personEntity.setBiography(personEntity.getBiography());
        personEntity.setBirthday(personEntity.getBirthday());
        personEntity.setDeathday(personEntity.getDeathday());
        db.getPeople().save(personEntity);
    }

    @Override
    public List<PersonDto> getPeople(Optional<String> keyword, Optional<Integer> currentPageOptional) {
        final int USERS_PER_PAGE = 10;
        Function<Person,PersonDto> personMapper = person -> {
            var personDto = new PersonDto();
            personDto.setId(person.getId());
            personDto.setFirstName(person.getFirstName());
            personDto.setLastName(person.getLastName());
            return personDto;
        };

        int currentPage = currentPageOptional.orElse(1);
        Pageable pageable = PageRequest.of(currentPage - 1, USERS_PER_PAGE);

        if (keyword.isPresent()) {
            return db.getPeople().findPeopleByByKeyword(keyword.get(), pageable).stream().map(personMapper).toList();
        }
        return db.getPeople().findAll(pageable).getContent().stream().map(personMapper).toList();
    }

    @Override
    @CachePut(cacheNames = "cachePersonDetails", key = "#personId")
    public PersonDetailedDto updatePerson(Long personId, PersonDetailedDto personDetailedDto) throws PersonNotFoundException {
        var personEntity = db.getPeople().findById(personId)
                .orElseThrow(() -> new PersonNotFoundException("Can't find person with id: " + personId));

        personEntity.setFirstName(personDetailedDto.getFirstName());
        personEntity.setLastName(personDetailedDto.getLastName());
        personEntity.setBiography(personDetailedDto.getBiography());
        personEntity.setBirthday(personDetailedDto.getBirthday());
        personEntity.setDeathday(personDetailedDto.getDeathday());

        db.getPeople().save(personEntity);
        return personDetailedDto;
    }

    @Override
    @CacheEvict(cacheNames = "cachePersonDetails", key = "#personId")
    public void deletePerson(Long personId) throws PersonNotFoundException {
        var personEntity = db.getPeople().findById(personId)
                .orElseThrow(() -> new PersonNotFoundException("Can't find person with id: " + personId));

        personEntity.getMoviesCreated().forEach(movie -> {
            movie.removeProducer(personEntity);
            db.getMovies().save(movie);
        });

        personEntity.getMoviesStarred().forEach(roleCharacter -> {
            var movieEntity = roleCharacter.getMovie();
            roleCharacter.removeActor();
            db.getMovies().save(movieEntity);
            db.getRoleCharacters().save(roleCharacter);
        });

        db.getPeople().deleteById(personId);
    }

    @Override
    @Cacheable(cacheNames = "cachePersonDetails", key = "#personId")
    public PersonDetailedDto getPerson(Long personId) throws PersonNotFoundException {
        var personEntity = db.getPeople().findById(personId)
                .orElseThrow(() -> new PersonNotFoundException("Can't find person with id: " + personId));

        var personDto = new PersonDetailedDto();
        personDto.setFirstName(personEntity.getFirstName());
        personDto.setLastName(personEntity.getLastName());
        personDto.setBiography(personEntity.getBiography());
        personDto.setBirthday(personEntity.getBirthday());
        personDto.setDeathday(personEntity.getDeathday());
        return personDto;
    }

    @Override
    @Cacheable(cacheNames = "cachePersonRoleList", key = "#personId")
    public RoleCharacterList getRoles(Long personId) throws PersonNotFoundException {
        db.getPeople().findById(personId)
                .orElseThrow(() -> new PersonNotFoundException("Can't find person with id: " + personId));

        List<RoleCharacterDto> roleCharacterDtos = db.getRoleCharacters().getPersonRoleCharactersByRating(personId).stream().map(roleCharacter -> {
            var roleCharacterDto = new RoleCharacterDto();
            roleCharacterDto.setId(roleCharacter.getId());
            roleCharacterDto.setName(roleCharacter.getName());
            return roleCharacterDto;
        }).toList();

        return new RoleCharacterList(roleCharacterDtos);
    }

    @Override
    @Cacheable(cacheNames = "cachePersonProductionsList", key = "#personId")
    public MovieList getMovies(Long personId) throws PersonNotFoundException {
        db.getPeople().findById(personId)
                .orElseThrow(() -> new PersonNotFoundException("Can't find person with id: " + personId));

        List<MovieDto> movieDtos = db.getMovies().getPersonMoviesByRating(personId).stream().map(movie -> {
            var movieDto = new MovieDto();
            movieDto.setId(movie.getId());
            movieDto.setTitle(movie.getTitle());
            return movieDto;
        }).toList();
        return new MovieList(movieDtos);
    }
}
