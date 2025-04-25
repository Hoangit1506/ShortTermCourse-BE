package com.short_term_course.entities;

import com.short_term_course.enums.LearningStatus;
import com.short_term_course.enums.TuitionStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "members")
@Entity
public class Member {
    @EmbeddedId
    @Column(name = "member_id")
    MemberId id;

    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "student_id", nullable = false)
    Account student;

    @ManyToOne
    @MapsId("classroomId")
    @JoinColumn(name = "classroom_id", nullable = false)
    Classroom classroom;

    @Column(name = "member_score")
    Double score; 

    @Enumerated(EnumType.STRING)
    LearningStatus learningStatus;

    @Enumerated(EnumType.STRING)
    TuitionStatus tuitionStatus;
}
