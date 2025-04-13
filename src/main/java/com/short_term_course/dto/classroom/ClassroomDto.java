package com.short_term_course.dto.classroom;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClassroomDto {
    String id;
    String name;
    String courseId;
    String courseName;
    String lecturerId;
    String lecturerName;
    LocalDate startDate;
    LocalDate endDate;
    String place;
    int capacity;
    int enrolled;
}

