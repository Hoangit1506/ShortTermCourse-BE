package com.short_term_course.controller;

import com.short_term_course.dto.api.ApiResponse;
import com.short_term_course.dto.classroom.ClassroomDto;
import com.short_term_course.dto.classroom.CreateClassroomRequest;
import com.short_term_course.dto.classroom.UpdateClassroomRequest;
import com.short_term_course.dto.pagination.PagedResponse;
import com.short_term_course.service.ClassroomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/classrooms")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class ClassroomController {
    private final ClassroomService service;

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<ClassroomDto>>> list(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.<PagedResponse<ClassroomDto>>builder()
                .data(service.list(pageable)).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClassroomDto>> get(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.<ClassroomDto>builder()
                .data(service.getById(id)).build());
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<ClassroomDto>> create(@RequestBody @Valid CreateClassroomRequest dto) {
        return ResponseEntity.status(201).body(ApiResponse.<ClassroomDto>builder()
                .data(service.create(dto)).build());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<ClassroomDto>> update(@PathVariable String id, @RequestBody UpdateClassroomRequest dto) {
        return ResponseEntity.ok(ApiResponse.<ClassroomDto>builder()
                .data(service.update(id, dto)).build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder().build());
    }
}
