package com.short_term_course.service;

import com.short_term_course.dto.category.CategoryDto;
import com.short_term_course.dto.category.CreateCategoryRequest;
import com.short_term_course.dto.category.UpdateCategoryRequest;
import com.short_term_course.dto.pagination.PagedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
//    List<CategoryDto> getAll();
//    Page<CategoryDto> getAll(Pageable pageable);
    PagedResponse<CategoryDto> getAll(Pageable pageable);
    CategoryDto getById(String id);
    CategoryDto create(CreateCategoryRequest dto);
    CategoryDto update(String id, UpdateCategoryRequest dto);
    void delete(String id);
}
