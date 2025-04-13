package com.short_term_course.dto.classroom;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateClassroomRequest {
    String name;
    String lecturerId;
    LocalDate startDate;
    LocalDate endDate;
    String place;
    Integer capacity;
}
