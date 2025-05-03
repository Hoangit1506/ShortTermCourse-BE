package com.short_term_course.dto.lecturer;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateLecturerRequest {
    @NotBlank @Email
    String email;

    @NotBlank @Size(min=3)
    String password;

    @NotBlank
    String displayName;

    LocalDate dob;

    String phoneNumber;
    String avatar;

    String position;
    String degree;

    @NotEmpty(message="Phải chọn ít nhất 1 chuyên ngành")
    Set<String> specializationIds; 
}
