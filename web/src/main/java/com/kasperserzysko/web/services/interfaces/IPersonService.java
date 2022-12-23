package com.kasperserzysko.web.services.interfaces;

import com.kasperserzysko.web.dtos.PersonDetailedDto;
import com.kasperserzysko.web.dtos.PersonDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IPersonService {

    void addPerson(PersonDetailedDto personDetailedDto);
    List<PersonDto> getPeople(String keyword, Integer currentPage);
    void updatePerson(Long id, PersonDetailedDto personDetailedDto);
    void deletePerson(Long id);
}
