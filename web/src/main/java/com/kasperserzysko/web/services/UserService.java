package com.kasperserzysko.web.services;

import com.kasperserzysko.data.models.Person;
import com.kasperserzysko.data.models.User;
import com.kasperserzysko.data.repositories.DataRepository;
import com.kasperserzysko.web.dtos.*;
import com.kasperserzysko.web.services.interfaces.IUserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

@Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
@Service
public class UserService implements UserDetailsService, IUserService {

    private final DataRepository db;
    private final PasswordEncoder passwordEncoder;
    private final static int ITEMS_PER_PAGE = 10;

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
        userEntity.addRole(db.getRoles().getRole("ROLE_USER").get());
        db.getUsers().save(userEntity);
    }

    @Override
    public List<UserDto> getUsers(String keyword, Integer currentPage) {
        Function<User, UserDto> userMapper = user -> {
            var userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setEmail(user.getEmail());
            return userDto;
        };
        if (currentPage == null){
            currentPage = 1;
        }

        Pageable pageable = PageRequest.of(currentPage - 1, ITEMS_PER_PAGE);
        if (keyword != null ) {
            return db.getUsers().getUsers(keyword, pageable).stream().map(userMapper).toList();
        }
        return db.getUsers().findAll(pageable).stream().map(userMapper).toList();
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

            userEntity.getCommentsLiked().forEach(comment -> {
                userEntity.removeCommentLike(comment);
                db.getComments().save(comment);
            });
            userEntity.getCommentsDisliked().forEach(comment -> {
                userEntity.removeCommentDislike(comment);
                db.getComments().save(comment);
            });
            userEntity.getComments().forEach(comment -> {
                userEntity.removeComment(comment);
                db.getComments().save(comment);
            });
            userEntity.getMoviesLiked().forEach(movie -> {
                userEntity.removeLikeOrDislike(movie);
                db.getMovies().save(movie);
            });
            userEntity.getMoviesDisliked().forEach(movie -> {
                userEntity.removeLikeOrDislike(movie);
                db.getMovies().save(movie);
            });
            userEntity.getRolesLiked().forEach(roleCharacter -> {
                userEntity.removeRoleLike(roleCharacter);
                db.getRoleCharacters().save(roleCharacter);
            });
            userEntity.getRolesDisliked().forEach(roleCharacter -> {
                userEntity.removeRoleDislike(roleCharacter);
                db.getRoleCharacters().save(roleCharacter);
            });
            userEntity.getRoles().forEach(role -> {
                userEntity.removeRole(role);
                db.getRoles().save(role);
            });

            db.getMovies().deleteById(id);
        }
    }

    @Override
    public List<MovieDto> getLikedMovies(Long userId, Integer currentPage) {
        if (currentPage == null){
            currentPage = 1;
        }
        Pageable pageable = PageRequest.of(currentPage - 1, ITEMS_PER_PAGE);
        return db.getMovies().getLikedMovies(userId, pageable).stream().map(movie -> {
            var movieDto = new MovieDto();
            movieDto.setId(movie.getId());
            movieDto.setTitle(movie.getTitle());
            return movieDto;
        }).toList();
    }

    @Override
    public List<RoleCharacterDto> getLikedRoleCharacters(Long userId, Integer currentPage) {
        if (currentPage == null){
            currentPage = 1;
        }
        Pageable pageable = PageRequest.of(currentPage - 1, ITEMS_PER_PAGE);
        return db.getRoleCharacters().getLikedRoleCharacters(userId, pageable).stream().map(roleCharacter -> {
            var roleCharacterDto = new RoleCharacterDto();
            roleCharacterDto.setId(roleCharacter.getId());
            roleCharacterDto.setName(roleCharacter.getName());
            return roleCharacterDto;
        }).toList();
    }

    @Override
    public List<CommentDto> getComments(Long userId, Integer currentPage) {
        if (currentPage == null){
            currentPage = 1;
        }
        Pageable pageable = PageRequest.of(currentPage - 1, ITEMS_PER_PAGE);
        return db.getComments().getUserComments(userId, pageable).stream().map(comment -> {
            var commentDto = new CommentDto();
            commentDto.setTitle(comment.getTitle());
            commentDto.setContent(comment.getContent());
            return commentDto;
        }).toList();
    }

    @Override
    public List<CommentDto> getLikedComments(Long userId, Integer currentPage) {
        if (currentPage == null){
            currentPage = 1;
        }
        Pageable pageable = PageRequest.of(currentPage - 1, ITEMS_PER_PAGE);
        return db.getComments().getLikedComments(userId, pageable).stream().map(comment -> {
            var commentDto = new CommentDto();
            commentDto.setTitle(comment.getTitle());
            commentDto.setContent(comment.getContent());
            return commentDto;
        }).toList();
    }
}
