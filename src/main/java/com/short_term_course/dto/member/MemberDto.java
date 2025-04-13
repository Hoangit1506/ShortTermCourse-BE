package com.short_term_course.dto.member;

import com.short_term_course.enums.LearningStatus;
import com.short_term_course.enums.TuitionStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MemberDto {
    String classroomId;
    String classroomName;
    String studentId;
    String studentName;
    Double score;
    LearningStatus learningStatus;
    TuitionStatus tuitionStatus;
}
