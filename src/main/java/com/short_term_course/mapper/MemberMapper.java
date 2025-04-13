package com.short_term_course.mapper;

import com.short_term_course.dto.member.MemberDto;
import com.short_term_course.entities.Member;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    @Mapping(source="id.classroomId", target="classroomId")
    @Mapping(source="classroom.name",   target="classroomName")
    @Mapping(source="id.studentId",     target="studentId")
    @Mapping(source="student.displayName", target="studentName")
    MemberDto toDto(Member member);

    List<MemberDto> toDtoList(List<Member> members);
}
