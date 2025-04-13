package com.short_term_course.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "classrooms")
@Entity
public class Classroom {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "classroom_id")
    String id;

    @Column(name = "classroom_name", nullable = false)
    String name;

    @Column(name = "classroom_startdate")
    LocalDate startDate;

    @Column(name = "classroom_enddate")
    LocalDate endDate;

    @Column(name = "classroom_place")
    String place;  // Địa điểm học

    @Column(name = "classroom_capacity")
    int capacity;  // Số học viên tối đa

    @Column(name = "classroom_enrolled")
    int enrolled; // Số học viên đã đăng ký

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    Course course;

    @ManyToOne
    @JoinColumn(name = "lecturer_id", nullable = false)
    Account lecturer; // Giảng viên phụ trách lớp

    @OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL)
    List<Member> members = new ArrayList<>();
}
