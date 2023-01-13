package com.kasperserzysko.web.cache.list_models;

import com.kasperserzysko.web.dtos.RoleCharacterDto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RoleCharacterList extends ArrayList<RoleCharacterDto> implements Serializable {

    private final List<RoleCharacterDto> roleCharacterDtos;


    public RoleCharacterList(List<RoleCharacterDto> roleCharacterDtos) {
        this.roleCharacterDtos = roleCharacterDtos;
    }

    public List<RoleCharacterDto> getRoleCharacterDtos() {
        return roleCharacterDtos;
    }
}
