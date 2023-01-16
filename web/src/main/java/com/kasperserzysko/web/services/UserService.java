package com.kasperserzysko.web.services;

import com.kasperserzysko.data.models.User;
import com.kasperserzysko.data.repositories.DataRepository;
import com.kasperserzysko.web.dtos.*;
import com.kasperserzysko.web.exceptions.UserNotFoundException;
import com.kasperserzysko.web.services.interfaces.IUserService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
import java.util.Optional;
import java.util.function.Function;

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
    public List<UserDto> getUsers(Optional<String> keyword, Optional<Integer> currentPageOptional) {
        Function<User, UserDto> userMapper = user -> {
            var userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setEmail(user.getEmail());
            return userDto;
        };
        int currentPage = currentPageOptional.orElse(1);

        Pageable pageable = PageRequest.of(currentPage - 1, ITEMS_PER_PAGE);
        if (keyword.isPresent()) {
            return db.getUsers().getUsers(keyword.get(), pageable).stream().map(userMapper).toList();
        }
        return db.getUsers().findAll(pageable).stream().map(userMapper).toList();
    }


    @Override
    @Cacheable(cacheNames = "cacheUserUsername", key = "#userId")
    public UserUsernameDto getUser(Long userId) throws UserNotFoundException {
        var userEntity = db.getUsers().findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Couldn't find user with id: " + userId));

        var userDto = new UserUsernameDto();
        userDto.setUsername(userEntity.getEmail());
        return userDto;
    }

    @Override
    public void updateUser(Long userId, UserDetailedDto dto) throws UserNotFoundException {
        var userEntity = db.getUsers().findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Couldn't find user with id: " + userId));

        var userDto = new UserDetailedDto();
        userEntity.setEmail(userDto.getEmail());
        userEntity.setPassword(passwordEncoder.encode(userDto.getPassword()));

        db.getUsers().save(userEntity);
    }

    @Override
    @CacheEvict(cacheNames = "cacheUserUsername", key = "#userId")
    public void deleteUser(Long userId) throws UserNotFoundException {
        var userEntity = db.getUsers().findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Couldn't find user with id: " + userId));

        for (int index = userEntity.getCommentsLiked().size() - 1; index >= 0; index--){
            var comment = userEntity.getCommentsLiked().get(index);
            comment.getUpVotes().remove(userEntity);
            userEntity.getCommentsLiked().remove(comment);
        }
        for (int index = userEntity.getCommentsDisliked().size() - 1; index >= 0; index--){
            var comment = userEntity.getCommentsDisliked().get(index);
            comment.getDownVotes().remove(userEntity);
            userEntity.getCommentsDisliked().remove(comment);
        }
        for (int index = userEntity.getComments().size() - 1; index >= 0; index--){
            var comment = userEntity.getComments().get(index);

            userEntity.getComments().remove(comment);
            comment.setCommentCreator(null);
            comment.removeVotes(comment);

            db.getComments().delete(comment);
        }
        for (int index = userEntity.getMoviesLiked().size() - 1; index >= 0; index--){
            var movie = userEntity.getMoviesLiked().get(index);
            movie.getLikes().remove(userEntity);
            userEntity.getMoviesLiked().remove(movie);
        }
        for (int index = userEntity.getMoviesDisliked().size() - 1; index >= 0; index--){
            var movie = userEntity.getMoviesDisliked().get(index);
            movie.getDislikes().remove(userEntity);
            userEntity.getMoviesDisliked().remove(movie);
        }
        for (int index = userEntity.getRolesLiked().size() - 1; index >= 0; index--){
            var roleCharacter = userEntity.getRolesLiked().get(index);
            roleCharacter.getRoleLikes().remove(userEntity);
            userEntity.getRolesLiked().remove(roleCharacter);
        }
        for (int index = userEntity.getRolesDisliked().size() - 1; index >= 0; index--){
            var roleCharacter = userEntity.getRolesDisliked().get(index);
            roleCharacter.getRoleDislikes().remove(userEntity);
            userEntity.getRolesDisliked().remove(roleCharacter);
        }
        for (int index = userEntity.getRoles().size() - 1; index >= 0; index--){
            var role = userEntity.getRoles().get(index);
            role.getUsers().remove(userEntity);
            userEntity.getRoles().remove(role);
        }

        db.getUsers().deleteById(userId);
    }

    @Override
    public List<MovieDto> getLikedMovies(Long userId, Optional<Integer> currentPageOptional) throws UserNotFoundException {
        db.getUsers().findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Couldn't find user with id: " + userId));

        int currentPage = currentPageOptional.orElse(1);
        Pageable pageable = PageRequest.of(currentPage - 1, ITEMS_PER_PAGE);

        return db.getMovies().getLikedMovies(userId, pageable).stream().map(movie -> {
            var movieDto = new MovieDto();
            movieDto.setId(movie.getId());
            movieDto.setTitle(movie.getTitle());
            return movieDto;
        }).toList();
    }

    @Override
    public List<RoleCharacterDto> getLikedRoleCharacters(Long userId, Optional<Integer> currentPageOptional) throws UserNotFoundException {
        db.getUsers().findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Couldn't find user with id: " + userId));

        int currentPage = currentPageOptional.orElse(1);

        Pageable pageable = PageRequest.of(currentPage - 1, ITEMS_PER_PAGE);
        return db.getRoleCharacters().getLikedRoleCharacters(userId, pageable).stream().map(roleCharacter -> {
            var roleCharacterDto = new RoleCharacterDto();
            roleCharacterDto.setId(roleCharacter.getId());
            roleCharacterDto.setName(roleCharacter.getName());
            return roleCharacterDto;
        }).toList();
    }

    @Override
    public List<CommentDto> getComments(Long userId, Optional<Integer> currentPageOptional) throws UserNotFoundException {
        db.getUsers().findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Couldn't find user with id: " + userId));

        int currentPage = currentPageOptional.orElse(1);

        Pageable pageable = PageRequest.of(currentPage - 1, ITEMS_PER_PAGE);
        return db.getComments().getUserComments(userId, pageable).stream().map(comment -> {
            var commentDto = new CommentDto();
            commentDto.setTitle(comment.getTitle());
            commentDto.setContent(comment.getContent());
            return commentDto;
        }).toList();
    }

    @Override
    public List<CommentDto> getLikedComments(Long userId, Optional<Integer> currentPageOptional) throws UserNotFoundException {
        db.getUsers().findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Couldn't find user with id: " + userId));

        int currentPage = currentPageOptional.orElse(1);
        Pageable pageable = PageRequest.of(currentPage - 1, ITEMS_PER_PAGE);
        return db.getComments().getLikedComments(userId, pageable).stream().map(comment -> {
            var commentDto = new CommentDto();
            commentDto.setTitle(comment.getTitle());
            commentDto.setContent(comment.getContent());
            return commentDto;
        }).toList();
    }
}
