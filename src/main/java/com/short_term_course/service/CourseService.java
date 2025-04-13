package com.short_term_course.service;

import com.short_term_course.dto.course.*;
import com.short_term_course.dto.pagination.PagedResponse;
import org.springframework.data.domain.Pageable;

public interface CourseService {
    PagedResponse<CourseDto> list(String categoryId, String keyword, Pageable pageable);
    CourseDto getById(String id);
    CourseDto create(CreateCourseRequest dto);
    CourseDto update(String id, UpdateCourseRequest dto);
    void delete(String id);
}
