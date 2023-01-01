package com.kasperserzysko.web.services;

import com.kasperserzysko.data.models.User;
import com.kasperserzysko.data.repositories.DataRepository;
import com.kasperserzysko.web.dtos.*;
import com.kasperserzysko.web.services.interfaces.IUserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService, IUserService {

    private final DataRepository db;
    private final PasswordEncoder passwordEncoder;

    public UserService(DataRepository db, PasswordEncoder passwordEncoder) {
        this.db = db;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return db.getUsers().findUserByEmail(username).map(SecurityUserDto::new).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }


    @Override
    public void addUser(UserDetailedDto dto) {
        var userEntity = new User();
        userEntity.setEmail(dto.getEmail());
        userEntity.setPassword(passwordEncoder.encode(dto.getPassword()));
        db.getUsers().save(userEntity);
    }

    @Override
    public List<UserDto> getUsers() {
        return db.getUsers().findAll().stream().map(user -> {
            var userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setEmail(user.getEmail());
            return userDto;
        }).toList();
    }

    @Override
    public UserUsernameDto getUser(Long id) {
        var oUserEntity = db.getUsers().findById(id);
        if (oUserEntity.isPresent()) {
            var userEntity = oUserEntity.get();
            var userDto = new UserUsernameDto();
            userDto.setUsername(userEntity.getEmail());
            return userDto;
        }
        return null;
    }

    @Override
    public void updateUser(Long id, UserDetailedDto dto) {
        var oUserEntity = db.getUsers().findById(id);
        if (oUserEntity.isPresent()) {
            var userEntity = oUserEntity.get();
            var userDto = new UserDetailedDto();
            userEntity.setEmail(userDto.getEmail());
            userEntity.setPassword(passwordEncoder.encode(userDto.getPassword()));

            db.getUsers().save(userEntity);
        }
    }

    @Override
    public void deleteUser(Long id) {
        var oUserEntity = db.getUsers().findById(id);
        if (oUserEntity.isPresent()) {
            var userEntity = oUserEntity.get();

            userEntity.getComments().forEach(comment -> {
                userEntity.removeComment(comment);
//                db.getRoleCharacters().save(roleCharacter); //TODO COMMENT SERVICE
            });
            userEntity.getCommentsLiked().forEach(comment -> {
                userEntity.removeCommentLike(comment);
//                db.getRoleCharacters().save(roleCharacter); //TODO COMMENT SERVICE
            });

            //TODO PAMIETAJ O SEKCJI KOMENTARZY TUTAJ


            db.getMovies().deleteById(id);
        }
    }
}
