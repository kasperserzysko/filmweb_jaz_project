package com.kasperserzysko.data.repositories;

import com.kasperserzysko.data.models.Person;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    @Query("SELECT p FROM Person p WHERE p.firstName LIKE %:keyword% OR p.lastName LIKE %:keyword%")
    List<Person> findPeopleByByKeyword(String keyword, Pageable pageable);
}
