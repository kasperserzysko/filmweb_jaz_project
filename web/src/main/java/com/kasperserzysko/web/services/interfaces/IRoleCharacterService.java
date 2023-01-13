package com.kasperserzysko.web.services.interfaces;

import com.kasperserzysko.web.dtos.PersonDto;
import com.kasperserzysko.web.dtos.RoleCharacterDto;
import com.kasperserzysko.web.dtos.SecurityUserDto;
import com.kasperserzysko.web.exceptions.RoleCharacterNotFoundException;
import com.kasperserzysko.web.exceptions.UserNotFoundException;

import java.util.List;

public interface IRoleCharacterService {
    List<RoleCharacterDto> getTopRoles();
    PersonDto getRolePerson(Long roleCharacterId) throws RoleCharacterNotFoundException;
    void likeRoleCharacter(Long roleId, SecurityUserDto securityUserDto) throws RoleCharacterNotFoundException, UserNotFoundException;
    int getRoleCharacterLikes(Long roleId) throws RoleCharacterNotFoundException;
    void dislikeRoleCharacter(Long roleId, SecurityUserDto securityUserDto) throws RoleCharacterNotFoundException, UserNotFoundException;
    int getRoleCharacterDislikes(Long roleId) throws RoleCharacterNotFoundException;
    void removeLike(Long roleId, SecurityUserDto securityUserDto) throws RoleCharacterNotFoundException, UserNotFoundException;
    void removeDislike(Long roleId, SecurityUserDto userDto) throws RoleCharacterNotFoundException, UserNotFoundException;
}
