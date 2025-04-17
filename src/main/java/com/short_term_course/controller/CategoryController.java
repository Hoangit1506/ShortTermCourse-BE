package com.short_term_course.controller;

import com.short_term_course.dto.category.*;
import com.short_term_course.dto.api.ApiResponse;
import com.short_term_course.dto.pagination.PagedResponse;
import com.short_term_course.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService service;

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<CategoryDto>>> getAllPaged(
            @RequestParam(required = false) String search,
            @PageableDefault(size = 10, sort = "name") Pageable pageable) {
        PagedResponse<CategoryDto> page = service.getAll(search, pageable);
        return ResponseEntity.ok(ApiResponse.<PagedResponse<CategoryDto>>builder()
                .data(page)
                .code("cat-s-01")
                .message("Get categories with pagination successfully")
                .build());
    }

    // 2. Lấy theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryDto>> getById(@PathVariable String id) {
        CategoryDto dto = service.getById(id);
        return ResponseEntity.ok(ApiResponse.<CategoryDto>builder()
                .data(dto)
                .code("cat-s-02")
                .message("Get category by id successfully")
                .build());
    }

    // 3. Tạo mới (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<CategoryDto>> create(
            @RequestBody @Valid CreateCategoryRequest dto) {
        CategoryDto result = service.create(dto);
        return ResponseEntity.status(201)
                .body(ApiResponse.<CategoryDto>builder()
                        .data(result)
                        .code("cat-s-03")
                        .message("Created category successfully")
                        .build());
    }

    // 4. Cập nhật (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<CategoryDto>> update(
            @PathVariable String id,
            @RequestBody @Valid UpdateCategoryRequest dto) {
        CategoryDto result = service.update(id, dto);
        return ResponseEntity.ok(ApiResponse.<CategoryDto>builder()
                .data(result)
                .code("cat-s-04")
                .message("Updated category successfully")
                .build());
    }

    // 5. Xóa (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .code("cat-s-05")
                .message("Deleted category successfully")
                .build());
    }
}
