package com.short_term_course.dto.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AuthLogOutRequest {
    @NotNull(message = "Refresh token must be not null")
    String refreshToken;
}
