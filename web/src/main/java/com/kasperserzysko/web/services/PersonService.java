package com.kasperserzysko.web.services;

import com.kasperserzysko.data.models.Person;
import com.kasperserzysko.data.repositories.DataRepository;
import com.kasperserzysko.web.cache.list_wrappers.MovieListWrapper;
import com.kasperserzysko.web.cache.list_wrappers.RoleCharacterListWrapper;
import com.kasperserzysko.web.dtos.MovieDto;
import com.kasperserzysko.web.dtos.PersonDetailedDto;
import com.kasperserzysko.web.dtos.PersonDto;
import com.kasperserzysko.web.dtos.RoleCharacterDto;
import com.kasperserzysko.web.exceptions.PersonNotFoundException;
import com.kasperserzysko.web.services.interfaces.IPersonService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
    public void deletePerson(Long personId) throws PersonNotFoundException {        //TODO
        var personEntity = db.getPeople().findById(personId)
                .orElseThrow(() -> new PersonNotFoundException("Can't find person with id: " + personId));


        for (int index = personEntity.getMoviesCreated().size() - 1; index >= 0; index--) {
            var movie = personEntity.getMoviesCreated().get(index);
            movie.getProducers().remove(personEntity);
            personEntity.getMoviesCreated().remove(movie);
        }
        for (int index = personEntity.getMoviesStarred().size() - 1; index >= 0; index--) {
            var roleCharacter = personEntity.getMoviesStarred().get(index);

            roleCharacter.setActor(null);
            personEntity.getMoviesStarred().remove(roleCharacter);
            roleCharacter.getMovie().getCharacters().remove(roleCharacter);
            roleCharacter.setMovie(null);

            roleCharacter.removeVotes(roleCharacter);

            db.getRoleCharacters().delete(roleCharacter);
        }

        db.getPeople().deleteById(personId);
    }

    @Override
    @Cacheable(cacheNames = "cachePersonDetails", key = "#personId")
    public PersonDetailedDto getPerson(Long personId) throws PersonNotFoundException {
        var personEntity = db.getPeople().findById(personId)
                .orElseThrow(() -> new PersonNotFoundException("Can't find person with id: " + personId));
        log.info("PERSON FROM DB");
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
    public RoleCharacterListWrapper getRoles(Long personId) throws PersonNotFoundException {
        db.getPeople().findById(personId)
                .orElseThrow(() -> new PersonNotFoundException("Can't find person with id: " + personId));
        log.info("ROLES FROM DB");
        List<RoleCharacterDto> roleCharacterDtos = db.getRoleCharacters().getPersonRoleCharactersByRating(personId).stream().map(roleCharacter -> {
            var roleCharacterDto = new RoleCharacterDto();
            roleCharacterDto.setId(roleCharacter.getId());
            roleCharacterDto.setName(roleCharacter.getName());
            return roleCharacterDto;
        }).toList();

        return new RoleCharacterListWrapper(roleCharacterDtos);
    }

    @Override
    @Cacheable(cacheNames = "cachePersonProductionsList", key = "#personId")
    public MovieListWrapper getMovies(Long personId) throws PersonNotFoundException {
        db.getPeople().findById(personId)
                .orElseThrow(() -> new PersonNotFoundException("Can't find person with id: " + personId));
        log.info("PRODUCERS FROM DB");
        List<MovieDto> movieDtos = db.getMovies().getPersonMoviesByRating(personId).stream().map(movie -> {
            var movieDto = new MovieDto();
            movieDto.setId(movie.getId());
            movieDto.setTitle(movie.getTitle());
            return movieDto;
        }).toList();
        return new MovieListWrapper(movieDtos);
    }
}
