package com.short_term_course.controller;

import com.short_term_course.dto.api.ApiResponse;
import com.short_term_course.dto.lecturer.CreateLecturerRequest;
import com.short_term_course.dto.lecturer.LecturerDto;
import com.short_term_course.dto.lecturer.UpdateLecturerRequest;
import com.short_term_course.dto.pagination.PagedResponse;
import com.short_term_course.service.LecturerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lecturers")
@RequiredArgsConstructor
public class LecturerController {
    private final LecturerService service;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<LecturerDto>>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String categoryId,
            @PageableDefault(size = 10, sort = "displayName", direction = Sort.Direction.ASC) Pageable pageable) {
        var page = service.list(keyword, categoryId, pageable);
        return ResponseEntity.ok(ApiResponse.<PagedResponse<LecturerDto>>builder()
                .data(page).build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LecturerDto>> get(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.<LecturerDto>builder()
                .data(service.getById(id)).build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<LecturerDto>> create(
            @RequestBody @Valid CreateLecturerRequest dto) {
        LecturerDto created = service.create(dto);
        return ResponseEntity.status(201)
                .body(ApiResponse.<LecturerDto>builder().data(created).build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<LecturerDto>> update(
            @PathVariable String id,
            @RequestBody @Valid UpdateLecturerRequest dto) {
        return ResponseEntity.ok(ApiResponse.<LecturerDto>builder()
                .data(service.update(id, dto)).build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder().build());
    }
}

