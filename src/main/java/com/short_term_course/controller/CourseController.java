package com.short_term_course.controller;

import com.short_term_course.dto.api.ApiResponse;
import com.short_term_course.dto.course.*;
import com.short_term_course.dto.pagination.PagedResponse;
import com.short_term_course.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService service;

    // 1. List có phân trang, filter
    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<CourseDto>>> list(
            @RequestParam(required=false) String categoryId,
            @RequestParam(required=false) String keyword,
            @PageableDefault(size=10, sort="name") Pageable pageable) {
        PagedResponse<CourseDto> page = service.list(categoryId, keyword, pageable);
        return ResponseEntity.ok(ApiResponse.<PagedResponse<CourseDto>>builder()
                .code("course-s-01").message("Fetched courses").data(page).build());
    }

    // 2. Chi tiết
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseDto>> detail(@PathVariable String id) {
        CourseDto dto = service.getById(id);
        return ResponseEntity.ok(ApiResponse.<CourseDto>builder()
                .code("course-s-02").message("Course detail").data(dto).build());
    }

    // 3. Tạo mới (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<CourseDto>> create(
            @RequestBody @Valid CreateCourseRequest dto) {
        CourseDto created = service.create(dto);
        return ResponseEntity.status(201)
                .body(ApiResponse.<CourseDto>builder()
                        .code("course-s-03").message("Course created").data(created).build());
    }

    // 4. Cập nhật (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<CourseDto>> update(
            @PathVariable String id,
            @RequestBody @Valid UpdateCourseRequest dto) {
        CourseDto updated = service.update(id, dto);
        return ResponseEntity.ok(ApiResponse.<CourseDto>builder()
                .code("course-s-04").message("Course updated").data(updated).build());
    }

    // 5. Xóa (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .code("course-s-05").message("Course deleted").build());
    }
}
