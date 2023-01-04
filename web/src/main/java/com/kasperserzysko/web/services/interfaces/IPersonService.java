package com.kasperserzysko.web.services.interfaces;

import com.kasperserzysko.web.dtos.MovieDto;
import com.kasperserzysko.web.dtos.PersonDetailedDto;
import com.kasperserzysko.web.dtos.PersonDto;
import com.kasperserzysko.web.dtos.RoleCharacterDto;

import java.util.List;

public interface IPersonService {

    void addPerson(PersonDetailedDto personDetailedDto);
    List<PersonDto> getPeople(String keyword, Integer currentPage);
    void updatePerson(Long id, PersonDetailedDto personDetailedDto);
    void deletePerson(Long id);
    PersonDetailedDto getPerson(Long id);
    List<RoleCharacterDto> getRoles(Long personId);
    List<MovieDto> getMovies(Long personId);
}
