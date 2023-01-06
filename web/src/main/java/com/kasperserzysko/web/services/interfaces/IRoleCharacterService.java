package com.kasperserzysko.web.services.interfaces;

import com.kasperserzysko.web.dtos.PersonDto;
import com.kasperserzysko.web.dtos.RoleCharacterDto;
import com.kasperserzysko.web.dtos.RoleCharacterMovieDto;
import com.kasperserzysko.web.dtos.SecurityUserDto;
import com.kasperserzysko.web.exceptions.RoleCharacterNotFoundException;

import java.util.List;

public interface IRoleCharacterService {
    List<RoleCharacterDto> getTopRoles();
    PersonDto getRolePerson(Long roleCharacterId) throws RoleCharacterNotFoundException;
    void likeRoleCharacter(Long roleId, SecurityUserDto securityUserDto) throws RoleCharacterNotFoundException;
    int getRoleCharacterLikes(Long roleId) throws RoleCharacterNotFoundException;
    void dislikeRoleCharacter(Long roleId, SecurityUserDto securityUserDto) throws RoleCharacterNotFoundException;
    int getRoleCharacterDislikes(Long roleId) throws RoleCharacterNotFoundException;
    void removeLikesOrDislikes(Long roleId, SecurityUserDto securityUserDto) throws RoleCharacterNotFoundException;
}
