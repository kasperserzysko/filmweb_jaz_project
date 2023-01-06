package com.kasperserzysko.web.services;

import com.kasperserzysko.data.models.Person;
import com.kasperserzysko.data.repositories.DataRepository;
import com.kasperserzysko.web.dtos.MovieDto;
import com.kasperserzysko.web.dtos.PersonDetailedDto;
import com.kasperserzysko.web.dtos.PersonDto;
import com.kasperserzysko.web.dtos.RoleCharacterDto;
import com.kasperserzysko.web.exceptions.PersonNotFoundException;
import com.kasperserzysko.web.services.interfaces.IPersonService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public List<PersonDto> getPeople(String keyword, Integer currentPage) {
        final int USERS_PER_PAGE = 10;
        Function<Person,PersonDto> personMapper = person -> {
            var personDto = new PersonDto();
            personDto.setId(person.getId());
            personDto.setFirstName(person.getFirstName());
            personDto.setLastName(person.getLastName());
            return personDto;
        };
        if (currentPage == null){
            currentPage = 1;
        }
        Pageable pageable = PageRequest.of(currentPage - 1, USERS_PER_PAGE);

        if (keyword != null ) {
            return db.getPeople().findPeopleByByKeyword(keyword, pageable).stream().map(personMapper).toList();
        }
        return db.getPeople().findAll(pageable).getContent().stream().map(personMapper).toList();
    }

    @Override
    public void updatePerson(Long personId, PersonDetailedDto personDetailedDto) throws PersonNotFoundException {
        var personEntity = db.getPeople().findById(personId)
                .orElseThrow(() -> new PersonNotFoundException("Can't find person with id: " + personId));

        personEntity.setFirstName(personDetailedDto.getFirstName());
        personEntity.setLastName(personDetailedDto.getLastName());
        personEntity.setBiography(personDetailedDto.getBiography());
        personEntity.setBirthday(personDetailedDto.getBirthday());
        personEntity.setDeathday(personDetailedDto.getDeathday());

        db.getPeople().save(personEntity);
    }

    @Override
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
    public List<RoleCharacterDto> getRoles(Long personId) throws PersonNotFoundException {
        db.getPeople().findById(personId)
                .orElseThrow(() -> new PersonNotFoundException("Can't find person with id: " + personId));

        return db.getRoleCharacters().getPersonRoleCharactersByRating(personId).stream().map(roleCharacter -> {
            var roleCharacterDto = new RoleCharacterDto();
            roleCharacterDto.setId(roleCharacter.getId());
            roleCharacterDto.setName(roleCharacter.getName());
            return roleCharacterDto;
        }).toList();
    }

    @Override
    public List<MovieDto> getMovies(Long personId) throws PersonNotFoundException {
        db.getPeople().findById(personId)
                .orElseThrow(() -> new PersonNotFoundException("Can't find person with id: " + personId));

        return db.getMovies().getPersonMoviesByRating(personId).stream().map(movie -> {
            var movieDto = new MovieDto();
            movieDto.setId(movie.getId());
            movieDto.setTitle(movie.getTitle());
            return movieDto;
        }).toList();
    }
}
