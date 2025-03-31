package com.ecommerce.chomoi.repository;

import com.ecommerce.chomoi.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    boolean existsByEmailAndIsLocalTrue(String email);
    Optional<Account> findByEmailAndIsLocalTrue(String email);
    Optional<Account> findByIsLocalFalseAndProviderNameAndProviderId(String providerName, String providerId);
}
