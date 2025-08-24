package com.rodiugurlu.authservice.repository;

import com.rodiugurlu.authservice.entity.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthUserRepository extends JpaRepository<AuthUser, Integer> {
    Optional<AuthUser> findByUsername(String username);

    Optional<AuthUser> findByEmail(String email);

    void deleteByUsername(String username);
}
