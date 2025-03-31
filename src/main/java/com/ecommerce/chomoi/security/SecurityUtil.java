package com.ecommerce.chomoi.security;

import com.ecommerce.chomoi.entities.Account;
import com.ecommerce.chomoi.entities.Shop;
import com.ecommerce.chomoi.enums.Role;
import com.ecommerce.chomoi.exception.AppException;
import com.ecommerce.chomoi.repository.AccountRepository;
import com.ecommerce.chomoi.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtil {
    private final AccountRepository accountRepository;
    private final ShopRepository shopRepository;

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
    public String getShopId() {
        return this.getShop().getId();
    }
    public Shop getShop() {
        Account account = this.getAccount();
         if (!account.getRoles().contains(Role.SHOP)) {
             throw new AppException(HttpStatus.FORBIDDEN, "Insufficient permissions", "auth-e-08");
         }
        return shopRepository.findByAccount(account)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Shop not found", "shop-e-01"));
    }
}
