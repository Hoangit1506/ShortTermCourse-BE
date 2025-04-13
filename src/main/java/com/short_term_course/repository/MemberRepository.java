package com.short_term_course.repository;

import com.short_term_course.entities.Member;
import com.short_term_course.entities.MemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member, MemberId> {
    List<Member> findByClassroomId(String classroomId);
    List<Member> findByStudentId(String studentId);
}
