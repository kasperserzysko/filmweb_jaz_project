package com.kasperserzysko.web.controllers;

import com.kasperserzysko.web.dtos.*;
import com.kasperserzysko.web.exceptions.GenreNotFoundException;
import com.kasperserzysko.web.exceptions.MovieNotFoundException;
import com.kasperserzysko.web.exceptions.PersonNotFoundException;
import com.kasperserzysko.web.exceptions.UserNotFoundException;
import com.kasperserzysko.web.services.MainService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<MovieDto>> getMovies(@RequestParam("page") Optional<Integer> currentPage, @RequestParam("keyword") Optional<String> keyword){
        return ResponseEntity.ok(mainService.getMovies().getMovies(keyword, currentPage));
    }


    @GetMapping("/{id}")
    public ResponseEntity<MovieDetailsDto> getMovie(@PathVariable("id") Long id){
        try {
            return ResponseEntity.ok(mainService.getMovies().getMovie(id));
        } catch (MovieNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity updateMovie(@PathVariable("id") Long id, @RequestBody MovieDetailsDto dto){
        try {
            return ResponseEntity.ok(mainService.getMovies().updateMovie(id, dto));
        } catch (MovieNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity deleteMovie(@PathVariable("id") Long id){
        try {
            mainService.getMovies().deleteMovie(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (MovieNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
    @GetMapping("/{id}/roles")
    public ResponseEntity<List<RoleCharacterDto>> getMovieRoles(@PathVariable("id") Long id){
        try {
            return ResponseEntity.ok(mainService.getMovies().getMovieRoles(id).getRoleCharacterDtos());
        } catch (MovieNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/{id}/roles")
    public ResponseEntity addMovieRoleCharacter(@PathVariable("id") Long id, @RequestBody RoleCharacterMovieDto dto){

        try {
            mainService.getMovies().addMovieRoleCharacter(id, dto);
        } catch (MovieNotFoundException | PersonNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}/genres")
    public ResponseEntity<List<GenreDto>> getMovieGenres(@PathVariable("id") Long id){
        try {
            return ResponseEntity.ok(mainService.getMovies().getMovieGenres(id).getGenreDtos());
        } catch (MovieNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/{id}/genres")
    public ResponseEntity addMovieGenre(@PathVariable("id") Long id, @RequestBody GenreIdDto dto){
        try {
            mainService.getMovies().addMovieGenre(id, dto);
        } catch (MovieNotFoundException | GenreNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}/producers")
    public ResponseEntity<List<PersonDto>> getMovieProducers(@PathVariable("id") Long movieId){
        try {
            return ResponseEntity.ok(mainService.getMovies().getMovieProduces(movieId).getPeopleDtos());
        } catch (MovieNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/{id}/producers")
    public ResponseEntity addMovieProducer(@PathVariable("id") Long id, @RequestBody PersonIdDto dto){
        try {
            mainService.getMovies().addMovieProducer(id, dto);
        } catch (MovieNotFoundException | PersonNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/like")
    public ResponseEntity likeMovie(@PathVariable("id") Long id, @AuthenticationPrincipal SecurityUserDto securityUser){
        try {
            mainService.getMovies().likeMovie(id, securityUser);
        } catch (MovieNotFoundException | UserNotFoundException e ) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}/like")
    public ResponseEntity<Integer> getMovieLikes(@PathVariable("id") Long id){
        try {
            return ResponseEntity.ok(mainService.getMovies().getMovieLikes(id));
        } catch (MovieNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/dislike")
    public ResponseEntity dislikeMovie(@PathVariable("id") Long id, @AuthenticationPrincipal SecurityUserDto securityUser){
        try {
            mainService.getMovies().dislikeMovie(id, securityUser);
        } catch (MovieNotFoundException | UserNotFoundException e ) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}/dislike")
    public ResponseEntity<Integer> getMovieDislikes(@PathVariable("id") Long id){
        try {
            return ResponseEntity.ok(mainService.getMovies().getMovieDislikes(id));
        } catch (MovieNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/comments")
    public ResponseEntity addComment(@PathVariable("id") Long id, @AuthenticationPrincipal SecurityUserDto securityUserDto, @RequestBody CommentDto dto){
        try {
            mainService.getMovies().addComment(id, dto, securityUserDto);
        } catch (MovieNotFoundException | UserNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentDetailedDto>> getComments(@PathVariable("id") Long id, @RequestParam("page") Optional<Integer> currentPage){
        try {
            return ResponseEntity.ok(mainService.getMovies().getComments(id, currentPage));
        } catch (MovieNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/top")
    public ResponseEntity<List<MovieDto>> getTopMovies(@RequestParam("page") Optional<Integer> currentPage){
        return ResponseEntity.ok(mainService.getMovies().getTopMovies(currentPage));
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}/like")
    public ResponseEntity removeLike(@PathVariable("id") Long id, @AuthenticationPrincipal SecurityUserDto securityUserDto){
        try {
            mainService.getMovies().removeLike(id,securityUserDto);
        } catch (MovieNotFoundException | UserNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}/dislike")
    public ResponseEntity removeDislike(@PathVariable("id") Long id, @AuthenticationPrincipal SecurityUserDto securityUserDto){
        try {
            mainService.getMovies().removeDislike(id,securityUserDto);
        } catch (MovieNotFoundException | UserNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
