package com.short_term_course.repository;

import com.short_term_course.entities.Account;
import com.short_term_course.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    boolean existsByEmailAndIsLocalTrue(String email);
    Optional<Account> findByEmailAndIsLocalTrue(String email);
    Optional<Account> findByIsLocalFalseAndProviderNameAndProviderId(String providerName, String providerId);
    Page<Account> findByRolesContains(Role role, Pageable pageable);
}
