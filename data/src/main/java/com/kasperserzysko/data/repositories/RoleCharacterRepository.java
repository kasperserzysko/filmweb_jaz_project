package com.kasperserzysko.data.repositories;

import com.kasperserzysko.data.models.RoleCharacter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleCharacterRepository extends JpaRepository<RoleCharacter, Long> {
}
