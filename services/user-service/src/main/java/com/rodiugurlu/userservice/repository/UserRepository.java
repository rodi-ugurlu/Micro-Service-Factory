package com.rodiugurlu.userservice.repository;

import com.rodiugurlu.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
    boolean existsByEmail(String email);

    void deleteUserById(int id);

    void deleteByUsername(String username);
}
