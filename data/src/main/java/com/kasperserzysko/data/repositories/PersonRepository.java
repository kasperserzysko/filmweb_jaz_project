package com.kasperserzysko.data.repositories;

import com.kasperserzysko.data.models.Person;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    @Query("SELECT p FROM Person p WHERE p.firstName LIKE %:keyword% OR p.lastName LIKE %:keyword%")
    List<Person> findPeopleByByKeyword(String keyword, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT p.* FROM person p JOIN movie_producers mp on p.id = mp.person_id WHERE mp.movie_id = :movieId")
    List<Person> getMovieProducers(Long movieId);

    @Query(nativeQuery = true, value = "SELECT p.* FROM person p JOIN role_character rc on p.id = rc.actor_id WHERE rc.id = :personId")
    Optional<Person> getRoleCharacterPerson(Long personId);
}
