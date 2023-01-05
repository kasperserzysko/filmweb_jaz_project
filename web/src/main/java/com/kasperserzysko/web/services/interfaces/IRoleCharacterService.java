package com.kasperserzysko.web.services.interfaces;

import com.kasperserzysko.web.dtos.PersonDto;
import com.kasperserzysko.web.dtos.RoleCharacterDto;
import com.kasperserzysko.web.dtos.RoleCharacterMovieDto;

import java.util.List;

public interface IRoleCharacterService {
    List<RoleCharacterDto> getTopRoles();
    PersonDto getRolePerson(Long roleCharacterId);
}
