package com.short_term_course.repository;

import com.short_term_course.entities.Course;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course,String> {
    boolean existsByName(String name);
    Page<Course> findByCategoryId(String categoryId, Pageable pageable);
    Page<Course> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
}
