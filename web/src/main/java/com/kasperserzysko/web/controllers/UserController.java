package com.kasperserzysko.web.controllers;

import com.kasperserzysko.web.dtos.*;
import com.kasperserzysko.web.exceptions.UserNotFoundException;
import com.kasperserzysko.web.services.MainService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final MainService mainService;

    public UserController(MainService mainService) {
        this.mainService = mainService;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers(@RequestParam("page") Integer currentPage, @RequestParam("keyword") String keyword){
        return ResponseEntity.ok(mainService.getUsers().getUsers(keyword, currentPage));
    }


    @PostMapping
    public ResponseEntity addUser(@RequestBody UserDetailedDto dto){
        mainService.getUsers().addUser(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserUsernameDto> getUser(@PathVariable("id") Long id){
        try {
            return ResponseEntity.ok(mainService.getUsers().getUser(id));
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity updateUser(@PathVariable("id") Long id, @RequestBody UserDetailedDto dto){
        try {
            mainService.getUsers().updateUser(id, dto);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable("id") Long id){
        try {
            mainService.getUsers().deleteUser(id);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}/likes_movies")
    public ResponseEntity<List<MovieDto>> getLikedMovies(@PathVariable("id") Long id, @RequestParam("page") Integer currentPage){
        try {
            return ResponseEntity.ok(mainService.getUsers().getLikedMovies(id,currentPage));
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}/likes_roles")
    public ResponseEntity<List<RoleCharacterDto>> getLikedRoles(@PathVariable("id") Long id, @RequestParam("page") Integer currentPage){
        try {
            return ResponseEntity.ok(mainService.getUsers().getLikedRoleCharacters(id, currentPage));
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentDto>> getComments(@PathVariable("id") Long id, @RequestParam("page") Integer currentPage){
        try {
            return ResponseEntity.ok(mainService.getUsers().getComments(id, currentPage));
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}/likes_comments")
    public ResponseEntity<List<CommentDto>> getLikedComments(@PathVariable("id") Long id, @RequestParam("page") Integer currentPage){
        try {
            return ResponseEntity.ok(mainService.getUsers().getLikedComments(id, currentPage));
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
