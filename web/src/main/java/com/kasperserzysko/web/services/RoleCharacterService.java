package com.kasperserzysko.web.services;

import com.kasperserzysko.data.models.RoleCharacter;
import com.kasperserzysko.data.repositories.DataRepository;
import com.kasperserzysko.web.dtos.RoleCharacterDto;
import com.kasperserzysko.web.dtos.RoleCharacterMovieDto;
import com.kasperserzysko.web.services.interfaces.IRoleCharacterService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleCharacterService implements IRoleCharacterService {

    private final DataRepository db;

    public RoleCharacterService(DataRepository db) {
        this.db = db;
    }




}
