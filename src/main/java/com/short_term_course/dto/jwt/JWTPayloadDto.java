package com.short_term_course.dto.jwt;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
//@NoArgsConstructor
//@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JWTPayloadDto {
    String id;
    String email;
    String scope;

    @JsonCreator
    public JWTPayloadDto(@JsonProperty("id") String id, @JsonProperty("email") String email, @JsonProperty("scope") String scope) {
        this.id = id;
        this.email = email;
        this.scope = scope;
    }
}
