package com.short_term_course.dto.auth;

import lombok.Data;
import java.util.Set;

@Data
public class AuthAccountInfoResponse {
    private String id;
    private String displayName;
    private String email;
    private String avatar;
    private Set<String> roles;
}
