package com.kasperserzysko.web.cache.list_wrappers;

import com.kasperserzysko.web.dtos.PersonDto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PersonListWrapper extends ArrayList<PersonDto> implements Serializable {

    private final List<PersonDto> peopleDtos;


    public PersonListWrapper(List<PersonDto> peopleDtos) {
        this.peopleDtos = peopleDtos;
    }

    public List<PersonDto> getPeopleDtos() {
        return peopleDtos;
    }
}
