package com.ecommerce.chomoi.repository;

import com.ecommerce.chomoi.entities.Account;
import com.ecommerce.chomoi.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    Optional<RefreshToken> findByAccountId(String userId);
    Optional<RefreshToken> findByAccount(Account account);
}
