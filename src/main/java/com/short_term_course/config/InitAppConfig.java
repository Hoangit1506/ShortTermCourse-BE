package com.short_term_course.config;

import com.short_term_course.enums.Role;
import com.short_term_course.entities.Account;
import com.short_term_course.repository.AccountRepository;
import com.short_term_course.util.PasswordUtil;
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

    //Bean kiểu ApplicationRunner được chạy sau khi ứng dụng khởi động.
    @Bean
    ApplicationRunner applicationRunner() {
        return args -> {
            boolean isExistedAdmin = AccountRepository.existsByEmailAndIsLocalTrue(ADMIN_EMAIL);
            if (isExistedAdmin) return;
            Set<Role> roles = EnumSet.allOf(Role.class);
            Account admin = Account.builder()
                    .email(ADMIN_EMAIL)
                    .displayName("Admin")
                    .password(passwordUtil.encodePassword(ADMIN_PASSWORD))
                    .roles(roles)
                    .isLocal(true)
                    .build();
            AccountRepository.save(admin);
        };
    }
}
