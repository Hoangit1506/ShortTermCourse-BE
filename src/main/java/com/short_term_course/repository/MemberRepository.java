package com.short_term_course.repository;

import com.short_term_course.entities.Member;
import com.short_term_course.entities.MemberId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, MemberId> {
    List<Member> findByClassroomId(String classroomId);
    List<Member> findByStudentId(String studentId);

    @Query("""
    SELECT m FROM Member m
    JOIN m.classroom c
    WHERE m.id.studentId = :studentId
    AND (:keyword IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')))
    ORDER BY c.startDate ASC, c.name ASC
    """)
    Page<Member> findMyCourses(
            @Param("studentId") String studentId,
            @Param("keyword") String keyword,
            Pageable pageable
    );

}
