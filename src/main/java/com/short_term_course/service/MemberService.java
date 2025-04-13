package com.short_term_course.service;

import com.short_term_course.dto.member.*;
import java.util.List;

public interface MemberService {
    MemberDto enroll(String studentId, CreateMemberRequest dto);
    List<MemberDto> listByClassroom(String classroomId);
    List<MemberDto> listByStudent(String studentId);
    MemberDto update(String classroomId, String studentId, UpdateMemberRequest dto);
    void cancel(String studentId, String classroomId);
    void remove(String classroomId, String studentId); // Lecturer/ADMIN
}
