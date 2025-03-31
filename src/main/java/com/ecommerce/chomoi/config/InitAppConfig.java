package com.ecommerce.chomoi.config;

import com.ecommerce.chomoi.enums.Role;
import com.ecommerce.chomoi.entities.Account;
import com.ecommerce.chomoi.repository.AccountRepository;
import com.ecommerce.chomoi.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.EnumSet;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class InitAppConfig {
    @Value("${app.admin.email}")
    private String ADMIN_EMAIL;

    @Value("${app.admin.password}")
    private String ADMIN_PASSWORD;

    private final AccountRepository AccountRepository;
    private final PasswordUtil passwordUtil;

    @Bean
    ApplicationRunner applicationRunner() {
        return args -> {
            boolean isExistedAdmin = AccountRepository.existsByEmailAndIsLocalTrue(ADMIN_EMAIL);
            if (isExistedAdmin) return;
            Set<Role> roles = EnumSet.allOf(Role.class);
            roles.remove(Role.SHOP);
            Account admin = Account.builder()
                    .email(ADMIN_EMAIL)
                    .displayName("Root Admin")
                    .password(passwordUtil.encodePassword(ADMIN_PASSWORD))
                    .roles(roles)
                    .isLocal(true)
                    .build();
            AccountRepository.save(admin);
        };
    }
}
