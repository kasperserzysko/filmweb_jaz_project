package com.kasperserzysko.web.services.interfaces;

import com.kasperserzysko.web.dtos.UserDetailedDto;
import com.kasperserzysko.web.dtos.UserDto;
import com.kasperserzysko.web.dtos.UserUsernameDto;

import java.util.List;

public interface IUserService {

    void addUser(UserDetailedDto dto);
    List<UserDto> getUsers();
    UserUsernameDto getUser(Long id);
    void updateUser(Long id, UserDetailedDto dto);
    void deleteUser(Long id);
}
