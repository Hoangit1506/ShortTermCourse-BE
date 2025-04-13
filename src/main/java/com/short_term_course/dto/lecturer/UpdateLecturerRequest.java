package com.short_term_course.dto.lecturer;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateLecturerRequest {
    String displayName;
    String phoneNumber;
    String avatar;

    String degree;
    String position;

    Set<String> specializationIds;
}
