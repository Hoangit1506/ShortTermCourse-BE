package com.short_term_course.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "courses")
@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "course_id")
    String id;

    @Column(name = "course_name", nullable = false, unique = true)
    String name;

    @Lob   //tránh giới hạn độ dài
    @Column(name = "course_description",
            columnDefinition = "LONGTEXT"
    )
    String description;

    @Lob   //tránh giới hạn độ dài
    @Column(
            name = "course_suitable",
            columnDefinition = "LONGTEXT"
    )
    String suitable;  // Đối tượng phù hợp

    @Column(name = "course_price")
    Double price;

    @Lob   //tránh giới hạn độ dài
    @Column(
            name = "course_content",
            columnDefinition = "LONGTEXT"
    )
    String content;

    @Column(name = "course_thumbnail", length = 1000)
    String thumbnail;

    @Column(name = "course_promo_video", length = 1000)
    String promoVideo;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    Category category;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    List<Classroom> classes = new ArrayList<>();
}
