package com.kasperserzysko.web.controllers;

import com.kasperserzysko.web.dtos.PersonDetailedDto;
import com.kasperserzysko.web.dtos.PersonDto;
import com.kasperserzysko.web.services.MainService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/people")
@RestController
public class PersonController {

    private final MainService mainService;


    public PersonController(MainService mainService) {
        this.mainService = mainService;
    }


    @PostMapping
    public ResponseEntity addPerson(@RequestBody PersonDetailedDto dto){
        mainService.getPeople().addPerson(dto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<PersonDto>> getPeople(@RequestParam("keyword") String keyword, @RequestParam("page") Integer currentPage){
        return ResponseEntity.ok(mainService.getPeople().getPeople(keyword, currentPage));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonDetailedDto> getPerson(@PathVariable("id") Long id){
        return ResponseEntity.ok(mainService.getPeople().getPerson(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity updatePerson(@PathVariable("id") Long id, @RequestBody PersonDetailedDto dto){
        mainService.getPeople().updatePerson(id, dto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletePerson(@PathVariable("id") Long id){
        mainService.getPeople().deletePerson(id);
        return new ResponseEntity(HttpStatus.OK);
    }

}
