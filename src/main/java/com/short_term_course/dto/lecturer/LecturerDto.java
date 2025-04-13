package com.short_term_course.dto.lecturer;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LecturerDto {
    String id;
    String email;
    String displayName;
    String phoneNumber;
    String avatar;

    String position;
    String degree;

    Set<String> specializationIds;
    Set<String> specializationNames;
}
