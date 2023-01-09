package com.kasperserzysko.web.services;

import com.kasperserzysko.data.models.Person;
import com.kasperserzysko.data.repositories.DataRepository;
import com.kasperserzysko.web.dtos.PersonDto;
import com.kasperserzysko.web.dtos.RoleCharacterDto;
import com.kasperserzysko.web.dtos.SecurityUserDto;
import com.kasperserzysko.web.exceptions.RoleCharacterNotFoundException;
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
    public PersonDto getRolePerson(Long roleCharacterId) throws RoleCharacterNotFoundException {
        var personEntity = db.getPeople().getRoleCharacterPerson(roleCharacterId)
                .orElseThrow(() -> new RoleCharacterNotFoundException("Can't find role character with id: " + roleCharacterId));

        var personDto = new PersonDto();
        personDto.setId(personEntity.getId());
        personDto.setFirstName(personEntity.getFirstName());
        personDto.setLastName(personEntity.getLastName());
        return personDto;
    }

    @Override
    public void likeRoleCharacter(Long roleCharacterId, SecurityUserDto securityUserDto) throws RoleCharacterNotFoundException {
        var loggedUser = securityUserDto.getUser();
        var roleCharacterEntity = db.getRoleCharacters().findById(roleCharacterId)
                .orElseThrow(() -> new RoleCharacterNotFoundException("Can't find role character with id: " + roleCharacterId));

        loggedUser.addRoleLike(roleCharacterEntity);

        db.getRoleCharacters().save(roleCharacterEntity);
        db.getUsers().save(loggedUser);
    }

    @Override
    public int getRoleCharacterLikes(Long roleCharacterId) throws RoleCharacterNotFoundException {
        var roleCharacterEntity = db.getRoleCharacters().findById(roleCharacterId)
                .orElseThrow(() -> new RoleCharacterNotFoundException("Can't find role character with id: " + roleCharacterId));
        return roleCharacterEntity.getRoleLikes().size();
    }

    @Override
    public void dislikeRoleCharacter(Long roleCharacterId, SecurityUserDto securityUserDto) throws RoleCharacterNotFoundException {
        var loggedUser = securityUserDto.getUser();
        var roleCharacterEntity = db.getRoleCharacters().findById(roleCharacterId)
                .orElseThrow(() -> new RoleCharacterNotFoundException("Can't find role character with id: " + roleCharacterId));

        loggedUser.addRoleDislike(roleCharacterEntity);

        db.getRoleCharacters().save(roleCharacterEntity);
        db.getUsers().save(loggedUser);
    }

    @Override
    public int getRoleCharacterDislikes(Long roleCharacterId) throws RoleCharacterNotFoundException {
        var roleCharacterEntity = db.getRoleCharacters().findById(roleCharacterId)
                .orElseThrow(() -> new RoleCharacterNotFoundException("Can't find role character with id: " + roleCharacterId));
        return roleCharacterEntity.getRoleDislikes().size();
    }

    @Override
    public void removeLike(Long roleId, SecurityUserDto securityUserDto) throws RoleCharacterNotFoundException {
        var loggedUser = securityUserDto.getUser();
        var roleCharacterEntity = db.getRoleCharacters().findById(roleId)
                .orElseThrow(() -> new RoleCharacterNotFoundException("Can't find role character with id: " + roleId));

        loggedUser.removeRoleLike(roleCharacterEntity);

        db.getRoleCharacters().save(roleCharacterEntity);
        db.getUsers().save(loggedUser);
    }

    @Override
    public void removeDislike(Long roleId, SecurityUserDto userDto) throws RoleCharacterNotFoundException {
        var loggedUser = userDto.getUser();
        var roleCharacterEntity = db.getRoleCharacters().findById(roleId)
                .orElseThrow(() -> new RoleCharacterNotFoundException("Can't find role character with id: " + roleId));

        loggedUser.removeRoleDislike(roleCharacterEntity);

        db.getRoleCharacters().save(roleCharacterEntity);
        db.getUsers().save(loggedUser);
    }
}
