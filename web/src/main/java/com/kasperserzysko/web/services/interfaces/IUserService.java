package com.kasperserzysko.web.services.interfaces;

import com.kasperserzysko.web.dtos.*;

import java.util.List;

public interface IUserService {

    void addUser(UserDetailedDto dto);
    List<UserDto> getUsers(String keyword, Integer currentPage);
    UserUsernameDto getUser(Long id);
    void updateUser(Long id, UserDetailedDto dto);
    void deleteUser(Long id);
    List<MovieDto> getLikedMovies(Long userId, Integer currentPage);
    List<RoleCharacterDto> getLikedRoleCharacters(Long userId, Integer currentPage);
    List<CommentDto> getComments(Long userId, Integer currentPage);
    List<CommentDto> getLikedComments(Long userId, Integer currentPage);
}
