package com.kasperserzysko.web.cache.list_wrappers;

import com.kasperserzysko.web.dtos.RoleCharacterDto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RoleCharacterListWrapper extends ArrayList<RoleCharacterDto> implements Serializable {

    private final List<RoleCharacterDto> roleCharacterDtos;


    public RoleCharacterListWrapper(List<RoleCharacterDto> roleCharacterDtos) {
        this.roleCharacterDtos = roleCharacterDtos;
    }

    public List<RoleCharacterDto> getRoleCharacterDtos() {
        return roleCharacterDtos;
    }
}
