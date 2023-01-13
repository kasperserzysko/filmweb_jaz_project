package com.kasperserzysko.web.services.interfaces;

import com.kasperserzysko.web.dtos.*;
import com.kasperserzysko.web.exceptions.UserNotFoundException;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    void addUser(UserDetailedDto dto);
    List<UserDto> getUsers(Optional<String> keyword, Optional<Integer> currentPage);
    UserUsernameDto getUser(Long id) throws UserNotFoundException;
    void updateUser(Long id, UserDetailedDto dto) throws UserNotFoundException;
    void deleteUser(Long id) throws UserNotFoundException;
    List<MovieDto> getLikedMovies(Long userId, Optional<Integer> currentPage) throws UserNotFoundException;
    List<RoleCharacterDto> getLikedRoleCharacters(Long userId, Integer currentPage) throws UserNotFoundException;
    List<CommentDto> getComments(Long userId, Optional<Integer> currentPage) throws UserNotFoundException;
    List<CommentDto> getLikedComments(Long userId, Optional<Integer> currentPage) throws UserNotFoundException;
}
