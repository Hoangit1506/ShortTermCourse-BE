package com.short_term_course.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "categories")
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "category_id")
    String id;

    @Column(name = "category_name", nullable = false, unique = true)
    String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    List<Course> courses = new ArrayList<>();

    // Optional: ngược lại relation ManyToMany
    @ManyToMany(mappedBy = "specializations")
    @JsonIgnore
    Set<LecturerProfile> lecturers = new HashSet<>();
}