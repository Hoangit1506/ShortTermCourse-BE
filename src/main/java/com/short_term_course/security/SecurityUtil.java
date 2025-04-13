package com.short_term_course.security;

import com.short_term_course.entities.Account;
import com.short_term_course.enums.Role;
import com.short_term_course.exception.AppException;
import com.short_term_course.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtil {
    private final AccountRepository accountRepository;

    public String getAccountId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "User is not authenticated", "auth-e-00");
        }
        return authentication.getName();
    }
    public Account getAccount(){
        return accountRepository.findById(this.getAccountId()).orElseThrow(()->
                        new AppException(HttpStatus.NOT_FOUND,"Account not found", "auth-e-01")
                );
    }
}
