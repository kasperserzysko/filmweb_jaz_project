package com.kasperserzysko.web.controllers;


import com.kasperserzysko.web.dtos.PersonDto;
import com.kasperserzysko.web.dtos.RoleCharacterDto;
import com.kasperserzysko.web.dtos.SecurityUserDto;
import com.kasperserzysko.web.exceptions.RoleCharacterNotFoundException;
import com.kasperserzysko.web.services.MainService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
        try {
            return ResponseEntity.ok(mainService.getRoleCharacters().getRolePerson(id));
        } catch (RoleCharacterNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/likes")
    public ResponseEntity likeRole(@PathVariable("id") Long id, @AuthenticationPrincipal SecurityUserDto securityUserDto){
        try {
            mainService.getRoleCharacters().likeRoleCharacter(id, securityUserDto);
        } catch (RoleCharacterNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}/likes")
    public ResponseEntity<Integer> getRoleLikes(@PathVariable("id") Long id){
        try {
            return ResponseEntity.ok(mainService.getRoleCharacters().getRoleCharacterLikes(id));
        } catch (RoleCharacterNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/dislikes")
    public ResponseEntity dislikeRole(@PathVariable("id") Long id, @AuthenticationPrincipal SecurityUserDto securityUserDto){
        try {
            mainService.getRoleCharacters().dislikeRoleCharacter(id, securityUserDto);
        } catch (RoleCharacterNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/{id}/dislikes")
    public ResponseEntity<Integer> getRoleDislikes(@PathVariable("id") Long id){
        try {
            return ResponseEntity.ok(mainService.getRoleCharacters().getRoleCharacterDislikes(id));
        } catch (RoleCharacterNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}/likes")
    public ResponseEntity removeLike(@PathVariable("id") Long id, @AuthenticationPrincipal SecurityUserDto securityUserDto){
        try {
            mainService.getRoleCharacters().removeLike(id, securityUserDto);
        } catch (RoleCharacterNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}/dislikes")
    public ResponseEntity removeDislike(@PathVariable("id") Long id, @AuthenticationPrincipal SecurityUserDto securityUserDto){
        try {
            mainService.getRoleCharacters().removeDislike(id, securityUserDto);
        } catch (RoleCharacterNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
