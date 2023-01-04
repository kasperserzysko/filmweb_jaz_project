package com.kasperserzysko.web.controllers;

import com.kasperserzysko.web.dtos.*;
import com.kasperserzysko.web.services.MainService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MainService mainService;

    public MovieController(MainService mainService) {
        this.mainService = mainService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity addMovie(@RequestBody MovieDetailsDto dto){
        mainService.getMovies().addMovie(dto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<MovieDto>> getMovies(@RequestParam("page") Integer currentPage, @RequestParam("keyword") String keyword){
        return ResponseEntity.ok(mainService.getMovies().getMovies(keyword, currentPage));
    }


    @GetMapping("/{id}")
    public ResponseEntity<MovieDetailsDto> getMovie(@PathVariable("id") Long id){
        return ResponseEntity.ok(mainService.getMovies().getMovie(id));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity updateMovie(@PathVariable("id") Long id, @RequestBody MovieDetailsDto dto){
        mainService.getMovies().updateMovie(id, dto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity deleteMovie(@PathVariable("id") Long id){
        mainService.getMovies().deleteMovie(id);
        return new ResponseEntity(HttpStatus.OK);
    }
    @GetMapping("/{id}/roles")
    public ResponseEntity<List<RoleCharacterDto>> getMovieRoles(@PathVariable("id") Long id){
        return ResponseEntity.ok(mainService.getMovies().getMovieRoles(id));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/{id}/roles")
    public ResponseEntity addMovieRoleCharacter(@PathVariable("id") Long id, @RequestBody RoleCharacterMovieDto dto){

        mainService.getMovies().addMovieRoleCharacter(id, dto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/{id}/genres")
    public ResponseEntity<List<GenreDto>> getMovieGenres(@PathVariable("id") Long id){
        return ResponseEntity.ok(mainService.getMovies().getMovieGenres(id));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/{id}/genres")
    public ResponseEntity addMovieGenre(@PathVariable("id") Long id, @RequestBody GenreIdDto dto){
        mainService.getMovies().addMovieGenre(id, dto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/{id}/producers")
    public ResponseEntity<List<PersonDto>> getMovieProducers(@PathVariable("id") Long movieId){
        return ResponseEntity.ok(mainService.getMovies().getMovieProduces(movieId));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/{id}/producers")
    public ResponseEntity addMovieProducer(@PathVariable("id") Long id, @RequestBody PersonIdDto dto){
        mainService.getMovies().addMovieProducer(id, dto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/like")
    public ResponseEntity likeMovie(@PathVariable("id") Long id, @AuthenticationPrincipal SecurityUserDto securityUser){
        mainService.getMovies().likeMovie(id, securityUser);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/{id}/like")
    public ResponseEntity<Integer> getMovieLikes(@PathVariable("id") Long id){
        return ResponseEntity.ok(mainService.getMovies().getMovieLikes(id));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/dislike")
    public ResponseEntity dislikeMovie(@PathVariable("id") Long id, @AuthenticationPrincipal SecurityUserDto securityUser){
        mainService.getMovies().dislikeMovie(id, securityUser);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/{id}/dislike")
    public ResponseEntity<Integer> getMovieDislikes(@PathVariable("id") Long id){
        return ResponseEntity.ok(mainService.getMovies().getMovieDislikes(id));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/comments")
    public ResponseEntity addComment(@PathVariable("id") Long id, @AuthenticationPrincipal SecurityUserDto securityUserDto, @RequestBody CommentDto dto){
        mainService.getMovies().addComment(id, dto, securityUserDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentDetailedDto>> getComments(@PathVariable("id") Long id, @RequestParam("page") Integer currentPage){
        return ResponseEntity.ok(mainService.getMovies().getComments(id, currentPage));
    }

    @GetMapping("/top")
    public ResponseEntity<List<MovieDto>> getTopMovies(@RequestParam("page") Integer currentPage){
        return ResponseEntity.ok(mainService.getMovies().getTopMovies(currentPage));
    }

}
