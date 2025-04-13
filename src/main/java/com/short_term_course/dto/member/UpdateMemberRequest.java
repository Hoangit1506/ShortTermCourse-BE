package com.short_term_course.dto.member;

import com.short_term_course.enums.LearningStatus;
import com.short_term_course.enums.TuitionStatus;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateMemberRequest {
    @DecimalMin(value="0.0", message="Score must be >= 0")
    @DecimalMax(value="10.0", message="Score must be <= 10")
    Double score;

    TuitionStatus tuitionStatus;
    LearningStatus learningStatus;
}
