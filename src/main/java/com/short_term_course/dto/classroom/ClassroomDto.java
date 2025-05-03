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
    String courseThumbnail;
    Long coursePrice;
    String courseDescription;
    String courseSuitable;
    String courseContent;
    String coursePromoVideo;
    String lecturerId;
    String lecturerName;
    LocalDate lecturerDob;
    String lecturerEmail;
    String lecturerPhone;
    String lecturerAvatar;
    LocalDate startDate;
    LocalDate endDate;
    String place;
    int capacity;
    int enrolled;
    String categoryName;
}

