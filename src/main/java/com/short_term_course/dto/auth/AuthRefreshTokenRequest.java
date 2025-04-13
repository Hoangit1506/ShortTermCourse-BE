package com.short_term_course.dto.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AuthRefreshTokenRequest {
    @NotNull(message = "Refresh token must be not null")
    String refreshToken;
}
