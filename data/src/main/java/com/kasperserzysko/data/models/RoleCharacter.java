package com.kasperserzysko.data.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class RoleCharacter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "rolesLiked")
    private List<User> roleLikes = new ArrayList<>();

    @ManyToMany(mappedBy = "rolesDisliked")
    private List<User> roleDislikes = new ArrayList<>();

    @ManyToOne
    private Person actor;

    @ManyToOne
    private Movie movie;


    public void addActor(Person person){
        person.getMoviesStarred().add(this);
        actor = person;
    }
    public void removeVotes(RoleCharacter roleCharacter){
        for (int indexRole = roleCharacter.getRoleLikes().size() - 1; indexRole >= 0; indexRole--) {
            var user = roleCharacter.getRoleLikes().get(indexRole);
            user.getRolesLiked().remove(roleCharacter);
            roleCharacter.getRoleLikes().remove(user);
        }
        for (int indexRole = roleCharacter.getRoleDislikes().size() - 1; indexRole >= 0; indexRole--) {
            var user = roleCharacter.getRoleDislikes().get(indexRole);
            user.getRolesDisliked().remove(roleCharacter);
            roleCharacter.getRoleDislikes().remove(user);
        }
    }

}
