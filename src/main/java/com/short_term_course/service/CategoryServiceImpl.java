package com.short_term_course.service;

import com.short_term_course.dto.category.*;
import com.short_term_course.dto.pagination.PagedResponse;
import com.short_term_course.entities.Category;
import com.short_term_course.exception.AppException;
import com.short_term_course.mapper.CategoryMapper;
import com.short_term_course.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repo;
    private final CategoryMapper mapper;

    @Override
    public PagedResponse<CategoryDto> getAll(String search, Pageable pageable) {
        Page<Category> page;
        if (search != null && !search.isBlank()) {
            page = repo.findByNameContainingIgnoreCase(search, pageable);
        } else {
            page = repo.findAll(pageable);
        }
        List<CategoryDto> dtos = page.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        return PagedResponse.<CategoryDto>builder()
                .content(dtos)
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    @Override
    public CategoryDto getById(String id) {
        Category c = repo.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Category not found"));
        return mapper.toDto(c);
    }

    @Override
    public CategoryDto create(CreateCategoryRequest dto) {
        if (repo.existsByName(dto.getName())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Category name already exists");
        }
        Category c = mapper.toEntity(dto);
        c = repo.save(c);
        return mapper.toDto(c);
    }

    @Override
    public CategoryDto update(String id, UpdateCategoryRequest dto) {
        Category c = repo.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Category not found"));
        if (!c.getName().equals(dto.getName()) && repo.existsByName(dto.getName())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Category name already exists");
        }
        mapper.updateEntityFromDto(dto, c);
        c = repo.save(c);
        return mapper.toDto(c);
    }

    @Override
    public void delete(String id) {
        Category c = repo.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Category not found"));
        if (!c.getCourses().isEmpty()) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Cannot delete category with courses");
        }
        repo.delete(c);
    }
}
