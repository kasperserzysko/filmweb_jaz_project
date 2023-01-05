package com.kasperserzysko.web.controllers;


import com.kasperserzysko.web.dtos.PersonDto;
import com.kasperserzysko.web.dtos.RoleCharacterDto;
import com.kasperserzysko.web.services.MainService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleCharacterController {

    private final MainService mainService;

    public RoleCharacterController(MainService mainService) {
        this.mainService = mainService;
    }

    @GetMapping
    public ResponseEntity<List<RoleCharacterDto>> getTopRoleCharacters(){
        return ResponseEntity.ok(mainService.getRoleCharacters().getTopRoles());
    }


    @GetMapping("/roles/{id}/person")
    public ResponseEntity<PersonDto> getRolePerson(@PathVariable("id") Long id){
        return ResponseEntity.ok(mainService.getRoleCharacters().getRolePerson(id));
    }
}
