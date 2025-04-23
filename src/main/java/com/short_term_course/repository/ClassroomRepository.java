package com.short_term_course.repository;

import com.short_term_course.entities.Classroom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface ClassroomRepository extends JpaRepository<Classroom, String> {
    @Query("""
        SELECT c FROM Classroom c
        WHERE (:keyword    IS NULL OR LOWER(c.course.name)    LIKE %:keyword%)
        AND (:courseId    IS NULL OR c.course.id = :courseId)
        AND (:categoryId IS NULL OR c.course.category.id = :categoryId)
        AND (:start IS NULL OR c.startDate >= :start)
        AND (:end IS NULL OR c.endDate <= :end)
    """)
    Page<Classroom> filterOpenCourses(
            @Param("keyword")    String keyword,
            @Param("courseId")   String courseId,
            @Param("categoryId") String categoryId,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end,
            Pageable pageable
    );
}

//ORDER BY c.startDate ASC, c.name ASC
//OR LOWER(c.course.name) LIKE CONCAT('%', LOWER(:keyword), '%')