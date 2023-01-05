package com.kasperserzysko.web.services;

import com.kasperserzysko.data.models.Person;
import com.kasperserzysko.data.models.RoleCharacter;
import com.kasperserzysko.data.repositories.DataRepository;
import com.kasperserzysko.web.dtos.PersonDto;
import com.kasperserzysko.web.dtos.RoleCharacterDto;
import com.kasperserzysko.web.dtos.RoleCharacterMovieDto;
import com.kasperserzysko.web.services.interfaces.IRoleCharacterService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleCharacterService implements IRoleCharacterService {

    private final DataRepository db;

    public RoleCharacterService(DataRepository db) {
        this.db = db;
    }


    @Override
    public List<RoleCharacterDto> getTopRoles() {
        return db.getRoleCharacters().getRoleCharactersByRating().stream().map(roleCharacter -> {
            var roleCharacterDto = new RoleCharacterDto();
            roleCharacterDto.setId(roleCharacter.getId());
            roleCharacterDto.setName(roleCharacter.getName());
            return roleCharacterDto;
        }).toList();
    }

    @Override
    public PersonDto getRolePerson(Long roleCharacterId) {
        Optional<Person> oPersonEntity = db.getPeople().getRoleCharacterPerson(roleCharacterId);

        if (oPersonEntity.isPresent()){
            var personEntity = oPersonEntity.get();
            var personDto = new PersonDto();
            personDto.setId(personEntity.getId());
            personDto.setFirstName(personEntity.getFirstName());
            personDto.setLastName(personEntity.getLastName());
            return personDto;
        }
        return null;
    }
}
