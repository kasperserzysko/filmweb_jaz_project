package com.kasperserzysko.web.controllers;

import com.kasperserzysko.web.dtos.MovieDto;
import com.kasperserzysko.web.dtos.PersonDetailedDto;
import com.kasperserzysko.web.dtos.PersonDto;
import com.kasperserzysko.web.dtos.RoleCharacterDto;
import com.kasperserzysko.web.exceptions.PersonNotFoundException;
import com.kasperserzysko.web.services.MainService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/people")
@RestController
public class PersonController {

    private final MainService mainService;

    public PersonController(MainService mainService) {
        this.mainService = mainService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity addPerson(@RequestBody PersonDetailedDto dto){
        mainService.getPeople().addPerson(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<PersonDto>> getPeople(@RequestParam("keyword") Optional<String> keyword, @RequestParam("page") Optional<Integer> currentPage){
        return ResponseEntity.ok(mainService.getPeople().getPeople(keyword, currentPage));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonDetailedDto> getPerson(@PathVariable("id") Long id){
        try {
            return ResponseEntity.ok(mainService.getPeople().getPerson(id));
        } catch (PersonNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity updatePerson(@PathVariable("id") Long id, @RequestBody PersonDetailedDto dto){
        try {
            return ResponseEntity.ok(mainService.getPeople().updatePerson(id, dto));
        } catch (PersonNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity deletePerson(@PathVariable("id") Long id){
        try {
            mainService.getPeople().deletePerson(id);
        } catch (PersonNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}/roles")
    public ResponseEntity<List<RoleCharacterDto>> getPersonRoles(@PathVariable("id") Long id){
        try {
            return ResponseEntity.ok(mainService.getPeople().getRoles(id).getRoleCharacterDtos());
        } catch (PersonNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}/productions")
    public ResponseEntity<List<MovieDto>> getPersonMovies(@PathVariable("id") Long id){
        try {
            return ResponseEntity.ok(mainService.getPeople().getMovies(id).getMovieDtos());
        } catch (PersonNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
