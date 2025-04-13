package com.short_term_course.mapper;

import com.short_term_course.dto.course.*;
import com.short_term_course.entities.Course;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    @Mapping(source="category.id",   target="categoryId")
    @Mapping(source="category.name", target="categoryName")
    CourseDto toDto(Course course);

    @Mapping(target="id", ignore=true)
    @Mapping(target="classes", ignore=true)
    @Mapping(target="thumbnail", source="dto.thumbnail")
    @Mapping(target="promoVideo", source="dto.promoVideo")
    @Mapping(target="category", ignore=true) // set trong service
    Course toEntity(CreateCourseRequest dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target="classes", ignore=true)
    @Mapping(target="thumbnail", source="dto.thumbnail")
    @Mapping(target="promoVideo", source="dto.promoVideo")
    @Mapping(target="category", ignore=true)
    void updateFromDto(UpdateCourseRequest dto, @MappingTarget Course course);
}
