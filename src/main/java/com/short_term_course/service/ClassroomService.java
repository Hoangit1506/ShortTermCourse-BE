package com.short_term_course.service;

import com.short_term_course.dto.classroom.ClassroomDto;
import com.short_term_course.dto.classroom.CreateClassroomRequest;
import com.short_term_course.dto.classroom.UpdateClassroomRequest;
import com.short_term_course.dto.pagination.PagedResponse;
import org.springframework.data.domain.Pageable;

public interface ClassroomService {
    ClassroomDto create(CreateClassroomRequest dto);
    ClassroomDto update(String id, UpdateClassroomRequest dto);
    ClassroomDto getById(String id);
    void delete(String id);
    PagedResponse<ClassroomDto> list(Pageable pageable);
    PagedResponse<ClassroomDto> filterOpenCourses(String categoryId, String startDate, String endDate, Pageable pageable);
}
