package com.rodiugurlu.authservice.repository;

import com.rodiugurlu.authservice.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Integer> {
    VerificationToken findByEmailAndCode(String email, String code);

    void deleteByEmail(String email);

    void deleteAllByEmail(String email);
}
