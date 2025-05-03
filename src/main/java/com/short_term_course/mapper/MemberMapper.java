package com.short_term_course.mapper;

import com.short_term_course.dto.member.MemberDto;
import com.short_term_course.entities.Member;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    @Mapping(source="id.classroomId", target="classroomId")
    @Mapping(source="classroom.name", target="classroomName")
    @Mapping(source="classroom.lecturer.displayName", target="lecturerName")
    @Mapping(source="classroom.startDate", target="startDate")
    @Mapping(source="classroom.endDate", target="endDate")
    @Mapping(source="classroom.place", target="place")
    @Mapping(source="classroom.capacity", target="capacity")
    @Mapping(source="classroom.enrolled", target="enrolled")
    @Mapping(source="id.studentId", target="studentId")
    @Mapping(source="student.displayName", target="studentName")
    @Mapping(source="student.dob", target="studentDob")
    @Mapping(source="student.email", target="studentEmail")
    @Mapping(source="student.phoneNumber", target="studentPhone")
    @Mapping(source="student.avatar", target="studentAvatar")
    MemberDto toDto(Member member);
    List<MemberDto> toDtoList(List<Member> members);
}
