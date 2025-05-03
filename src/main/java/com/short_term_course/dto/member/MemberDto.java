package com.short_term_course.dto.member;

import com.short_term_course.enums.LearningStatus;
import com.short_term_course.enums.TuitionStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MemberDto {
    String classroomId;
    String classroomName;
    String lecturerName;
    LocalDate startDate;
    LocalDate endDate;
    String place;
    int capacity;
    int enrolled;
    String studentId;
    String studentName;
    LocalDate studentDob;
    String studentEmail;
    String studentPhone;
    String studentAvatar;
    Double score;
    LearningStatus learningStatus;
    TuitionStatus tuitionStatus;
}
