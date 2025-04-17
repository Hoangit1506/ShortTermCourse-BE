package com.short_term_course.mapper;

import com.short_term_course.dto.classroom.ClassroomDto;
import com.short_term_course.entities.Classroom;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClassroomMapper {
    @Mapping(source = "course.id", target = "courseId")
    @Mapping(source = "course.name", target = "courseName")
    @Mapping(source="course.thumbnail",  target="courseThumbnail")
    @Mapping(source = "lecturer.id", target = "lecturerId")
    @Mapping(source = "lecturer.displayName", target = "lecturerName")
    //@Mapping(source = "lecturer.account.displayName", target = "lecturerName")
    ClassroomDto toDto(Classroom classroom);
}

