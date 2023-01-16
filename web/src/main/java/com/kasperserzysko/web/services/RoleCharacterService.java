package com.kasperserzysko.web.services;

import com.kasperserzysko.data.repositories.DataRepository;
import com.kasperserzysko.web.dtos.PersonDto;
import com.kasperserzysko.web.dtos.RoleCharacterDto;
import com.kasperserzysko.web.dtos.SecurityUserDto;
import com.kasperserzysko.web.exceptions.RoleCharacterNotFoundException;
import com.kasperserzysko.web.exceptions.UserNotFoundException;
import com.kasperserzysko.web.services.interfaces.IRoleCharacterService;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public void likeRoleCharacter(Long roleCharacterId, SecurityUserDto securityUserDto) throws RoleCharacterNotFoundException, UserNotFoundException {
        var loggedUser = securityUserDto.getUser();
        var roleCharacterEntity = db.getRoleCharacters().findById(roleCharacterId)
                .orElseThrow(() -> new RoleCharacterNotFoundException("Can't find role character with id: " + roleCharacterId));
        var loggedUserWithRolesLiked = db.getUsers().getUserWithRolesLiked(loggedUser.getId()).
                orElseThrow(() -> new UserNotFoundException("Couldn't find user with id: " + loggedUser.getId()));

        if (!roleCharacterEntity.getRoleLikes().contains(loggedUserWithRolesLiked)) {
            loggedUserWithRolesLiked.addRoleLike(roleCharacterEntity);

            db.getRoleCharacters().save(roleCharacterEntity);
            db.getUsers().save(loggedUserWithRolesLiked);
        }
    }

    @Override
    public int getRoleCharacterLikes(Long roleCharacterId) throws RoleCharacterNotFoundException {
        db.getRoleCharacters().findById(roleCharacterId)
                .orElseThrow(() -> new RoleCharacterNotFoundException("Can't find role character with id: " + roleCharacterId));
        return db.getRoleCharacters().getRoleLikes(roleCharacterId);
    }

    @Override
    public void dislikeRoleCharacter(Long roleCharacterId, SecurityUserDto securityUserDto) throws RoleCharacterNotFoundException, UserNotFoundException {
        var loggedUser = securityUserDto.getUser();
        var roleCharacterEntity = db.getRoleCharacters().findById(roleCharacterId)
                .orElseThrow(() -> new RoleCharacterNotFoundException("Can't find role character with id: " + roleCharacterId));
        var loggedUserWithRolesDisliked = db.getUsers().getUserWithRolesDisliked(loggedUser.getId()).
                orElseThrow(() -> new UserNotFoundException("Couldn't find user with id: " + loggedUser.getId()));

        if (!roleCharacterEntity.getRoleDislikes().contains(loggedUserWithRolesDisliked)) {
            loggedUserWithRolesDisliked.addRoleDislike(roleCharacterEntity);

            db.getRoleCharacters().save(roleCharacterEntity);
            db.getUsers().save(loggedUserWithRolesDisliked);
        }
    }

    @Override
    public int getRoleCharacterDislikes(Long roleCharacterId) throws RoleCharacterNotFoundException {
        db.getRoleCharacters().findById(roleCharacterId)
                .orElseThrow(() -> new RoleCharacterNotFoundException("Can't find role character with id: " + roleCharacterId));
        return db.getRoleCharacters().getRoleDislikes(roleCharacterId);
    }

    @Override
    public void removeLike(Long roleId, SecurityUserDto securityUserDto) throws RoleCharacterNotFoundException, UserNotFoundException {
        var loggedUser = securityUserDto.getUser();
        var roleCharacterEntity = db.getRoleCharacters().findById(roleId)
                .orElseThrow(() -> new RoleCharacterNotFoundException("Can't find role character with id: " + roleId));
        var loggedUserWithRolesLiked = db.getUsers().getUserWithRolesLiked(loggedUser.getId()).
                orElseThrow(() -> new UserNotFoundException("Couldn't find user with id: " + loggedUser.getId()));

        loggedUserWithRolesLiked.removeRoleLike(roleCharacterEntity);

        db.getRoleCharacters().save(roleCharacterEntity);
        db.getUsers().save(loggedUser);
    }

    @Override
    public void removeDislike(Long roleId, SecurityUserDto userDto) throws RoleCharacterNotFoundException, UserNotFoundException {
        var loggedUser = userDto.getUser();
        var roleCharacterEntity = db.getRoleCharacters().findById(roleId)
                .orElseThrow(() -> new RoleCharacterNotFoundException("Can't find role character with id: " + roleId));
        var loggedUserWithRolesDisliked = db.getUsers().getUserWithRolesDisliked(loggedUser.getId()).
                orElseThrow(() -> new UserNotFoundException("Couldn't find user with id: " + loggedUser.getId()));

        loggedUserWithRolesDisliked.removeRoleDislike(roleCharacterEntity);

        db.getRoleCharacters().save(roleCharacterEntity);
        db.getUsers().save(loggedUserWithRolesDisliked);
    }
}
