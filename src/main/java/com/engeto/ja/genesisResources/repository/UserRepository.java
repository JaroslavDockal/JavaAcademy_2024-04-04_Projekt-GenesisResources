package com.engeto.ja.genesisResources.repository;

import com.engeto.ja.genesisResources.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUuid(UUID uuid);
    User findByPersonID(String personID);
}

