package com.ecommerce.chomoi.dto.auth;

import lombok.Data;
import java.util.Set;

@Data
public class AuthAccountInfoResponse {
    private String id;
    private String displayName;
    private String email;
    private String avatar;
    private Double rating;
    private Set<String> roles;
}
