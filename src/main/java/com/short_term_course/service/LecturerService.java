package com.short_term_course.service;

import com.short_term_course.dto.lecturer.CreateLecturerRequest;
import com.short_term_course.dto.lecturer.LecturerDto;
import com.short_term_course.dto.lecturer.UpdateLecturerRequest;
import com.short_term_course.dto.pagination.PagedResponse;
import org.springframework.data.domain.Pageable;

public interface LecturerService {
    PagedResponse<LecturerDto> list(String keyword, String categoryId, Pageable pageable);
    LecturerDto getById(String id);
    LecturerDto create(CreateLecturerRequest dto);
    LecturerDto update(String id, UpdateLecturerRequest dto);
    void delete(String id);
}
