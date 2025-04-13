package com.short_term_course.dto.classroom;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateClassroomRequest {
    @NotBlank(message = "Name is required")
    String name;

    @NotNull(message = "Course ID is required")
    String courseId;

    @NotNull(message = "Lecturer ID is required")
    String lecturerId;

    @NotNull(message = "Start date is required")
    LocalDate startDate;

    @NotNull(message = "End date is required")
    LocalDate endDate;

    @NotBlank(message = "Place is required")
    String place;

    @Min(value = 1, message = "Capacity must be at least 1")
    int capacity;

}
