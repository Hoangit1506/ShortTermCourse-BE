package com.short_term_course.dto.member;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateMemberRequest {
    @NotBlank(message = "Classroom ID is required")
    String classroomId;
}
