package com.kasperserzysko.web.services.interfaces;

import com.kasperserzysko.web.dtos.MovieDto;
import com.kasperserzysko.web.dtos.PersonDetailedDto;
import com.kasperserzysko.web.dtos.PersonDto;
import com.kasperserzysko.web.dtos.RoleCharacterDto;
import com.kasperserzysko.web.exceptions.PersonNotFoundException;

import java.util.List;

public interface IPersonService {

    void addPerson(PersonDetailedDto personDetailedDto);
    List<PersonDto> getPeople(String keyword, Integer currentPage);
    void updatePerson(Long id, PersonDetailedDto personDetailedDto) throws PersonNotFoundException;
    void deletePerson(Long id) throws PersonNotFoundException;
    PersonDetailedDto getPerson(Long id) throws PersonNotFoundException;
    List<RoleCharacterDto> getRoles(Long personId) throws PersonNotFoundException;
    List<MovieDto> getMovies(Long personId) throws PersonNotFoundException;
}
