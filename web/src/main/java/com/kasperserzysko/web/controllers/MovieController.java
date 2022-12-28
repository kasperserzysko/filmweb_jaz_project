package com.kasperserzysko.web.controllers;

import com.kasperserzysko.web.dtos.*;
import com.kasperserzysko.web.services.MainService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MainService mainService;

    public MovieController(MainService mainService) {
        this.mainService = mainService;
    }

    @PostMapping
    public ResponseEntity addMovie(@RequestBody MovieDetailsDto dto){
        mainService.getMovies().addMovie(dto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<MovieDto>> getMovies(){
        return ResponseEntity.ok(mainService.getMovies().getMovies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDetailsDto> getMovie(@PathVariable("id") Long id){
        return ResponseEntity.ok(mainService.getMovies().getMovie(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity updateMovie(@PathVariable("id") Long id, @RequestBody MovieDetailsDto dto){
        mainService.getMovies().updateMovie(id, dto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteMovie(@PathVariable("id") Long id){
        mainService.getMovies().deleteMovie(id);
        return new ResponseEntity(HttpStatus.OK);
    }
    @GetMapping("/{id}/roles")
    public ResponseEntity<List<RoleCharacterDto>> getMovieRoles(@PathVariable("id") Long id){
        return ResponseEntity.ok(mainService.getMovies().getMovieRoles(id));
    }
    @PostMapping("/{id}/roles")
    public ResponseEntity addMovieRoleCharacter(@PathVariable("id") Long id, @RequestBody RoleCharacterMovieDto dto){

        mainService.getMovies().addMovieRoleCharacter(id, dto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/{id}/genres")
    public ResponseEntity<List<GenreDto>> getMovieGenres(@PathVariable("id") Long id){
        return ResponseEntity.ok(mainService.getMovies().getMovieGenres(id));
    }
    @PostMapping("/{id}/genres")
    public ResponseEntity addMovieGenre(@PathVariable("id") Long id, @RequestBody GenreIdDto dto){
        mainService.getMovies().addMovieGenre(id, dto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/{id}/producers")
    public ResponseEntity<List<PersonDto>> getMovieProducers(@PathVariable("id") Long movieId){
        return ResponseEntity.ok(mainService.getMovies().getMovieProduces(movieId));
    }

    @PostMapping("/{id}/producers")
    public ResponseEntity addMovieProducer(@PathVariable("id") Long id, @RequestBody PersonIdDto dto){
        mainService.getMovies().addMovieProducer(id, dto);
        return new ResponseEntity(HttpStatus.OK);
    }




}
