package com.short_term_course.dto.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AuthVerifyForgotPasswordRequest {
    @NotNull(message = "New password cannot be blank")
    private String newPassword;
    @NotNull(message = "Code cannot be blank")
    private String code;
}
