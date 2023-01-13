package com.kasperserzysko.web.controllers;

import com.kasperserzysko.web.dtos.SecurityUserDto;
import com.kasperserzysko.web.dtos.UserDto;
import com.kasperserzysko.web.exceptions.CommentNotFoundException;
import com.kasperserzysko.web.exceptions.UserNotFoundException;
import com.kasperserzysko.web.services.MainService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final MainService mainService;

    public CommentController(MainService mainService) {
        this.mainService = mainService;
    }

    @GetMapping("/{id}/user")
    public ResponseEntity<UserDto> getCommentCreator(@PathVariable("id") Long id){
        try {
            return ResponseEntity.ok(mainService.getComments().getCommentCreator(id));
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/likes")
    public ResponseEntity likeComment(@PathVariable("id") Long id, @AuthenticationPrincipal SecurityUserDto securityUserDto){
        try {
            mainService.getComments().likeComment(id, securityUserDto);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (CommentNotFoundException | UserNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}/likes")
    public ResponseEntity<Integer> getCommentLikes(@PathVariable("id") Long id){
        try {
            return ResponseEntity.ok(mainService.getComments().getCommentLikes(id));
        } catch (CommentNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/dislikes")
    public ResponseEntity dislikeComment(@PathVariable("id") Long id, @AuthenticationPrincipal SecurityUserDto securityUserDto){
        try {
            mainService.getComments().dislikeComment(id, securityUserDto);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (CommentNotFoundException | UserNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/{id}/dislikes")
    public ResponseEntity getCommentDislikes(@PathVariable("id") Long id){
        try {
            return ResponseEntity.ok(mainService.getComments().getCommentDislikes(id));
        } catch (CommentNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}/likes")
    public ResponseEntity removeLike(@PathVariable("id") Long id, @AuthenticationPrincipal SecurityUserDto securityUserDto){
        try {
            mainService.getComments().removeLike(id, securityUserDto);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (CommentNotFoundException | UserNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}/dislikes")
    public ResponseEntity removeDislike(@PathVariable("id") Long id, @AuthenticationPrincipal SecurityUserDto securityUserDto){
        try {
            mainService.getComments().removeDislike(id, securityUserDto);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (CommentNotFoundException | UserNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
