package com.api.banking_study.repository;

import com.api.banking_study.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    UserDetails findByEmail(String email);

}
