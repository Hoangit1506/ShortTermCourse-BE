package com.short_term_course.repository;

import com.short_term_course.entities.Account;
import com.short_term_course.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    Optional<RefreshToken> findByAccountId(String userId);
    Optional<RefreshToken> findByAccount(Account account);
}
