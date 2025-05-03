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
    @Mapping(source = "course.price", target = "coursePrice")
    @Mapping(source = "course.description", target = "courseDescription")
    @Mapping(source = "course.suitable", target = "courseSuitable")
    @Mapping(source = "course.content", target = "courseContent")
    @Mapping(source = "course.promoVideo", target = "coursePromoVideo")
    @Mapping(source = "lecturer.id", target = "lecturerId")
    @Mapping(source = "lecturer.displayName", target = "lecturerName")
    @Mapping(source = "lecturer.dob", target = "lecturerDob")
    @Mapping(source = "lecturer.email", target = "lecturerEmail")
    @Mapping(source = "lecturer.phoneNumber", target = "lecturerPhone")
    @Mapping(source = "lecturer.avatar", target = "lecturerAvatar")
    @Mapping(source = "course.category.name", target = "categoryName")
    ClassroomDto toDto(Classroom classroom);
}

