package com.short_term_course.service;

import com.short_term_course.dto.course.*;
import com.short_term_course.dto.pagination.PagedResponse;
import com.short_term_course.entities.*;
import com.short_term_course.exception.AppException;
import com.short_term_course.mapper.CourseMapper;
import com.short_term_course.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepo;
    private final CategoryRepository categoryRepo;
    private final CourseMapper mapper;

//    @Override
//    public PagedResponse<CourseDto> list(String categoryId, String keyword, Pageable pageable) {
//        Page<Course> page;
//        if (categoryId != null) {
//            page = courseRepo.findByCategoryId(categoryId, pageable);
//        } else if (keyword != null) {
//            page = courseRepo.findByNameContainingIgnoreCase(keyword, pageable);
//        } else {
//            page = courseRepo.findAll(pageable);
//        }
//        List<CourseDto> dtos = page.stream()
//                .map(mapper::toDto)
//                .collect(Collectors.toList());
//
//        return PagedResponse.<CourseDto>builder()
//                .content(dtos)
//                .pageNumber(page.getNumber())
//                .pageSize(page.getSize())
//                .totalElements(page.getTotalElements())
//                .totalPages(page.getTotalPages())
//                .last(page.isLast())
//                .build();
//    }

    @Override
    public PagedResponse<CourseDto> list(String categoryId, String keyword, Pageable pageable) {
        Page<Course> page;

        boolean hasCat = categoryId != null && !categoryId.isBlank();
        boolean hasKw  = keyword    != null && !keyword.isBlank();

        if (hasCat && hasKw) {
            // cả keyword + category
            page = courseRepo
                    .findByNameContainingIgnoreCaseAndCategoryId(keyword, categoryId, pageable);

        } else if (hasCat) {
            // chỉ filter theo chuyên ngành
            page = courseRepo.findByCategoryId(categoryId, pageable);

        } else if (hasKw) {
            // chỉ filter theo từ khóa tên
            page = courseRepo.findByNameContainingIgnoreCase(keyword, pageable);

        } else {
            // không có điều kiện filter
            page = courseRepo.findAll(pageable);
        }

        List<CourseDto> dtos = page.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        return PagedResponse.<CourseDto>builder()
                .content(dtos)
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }


    @Override
    public CourseDto getById(String id) {
        Course c = courseRepo.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Course not found", "course-e-01"));
        return mapper.toDto(c);
    }

    @Override
    public CourseDto create(CreateCourseRequest dto) {
        if (courseRepo.existsByName(dto.getName())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Course name exists", "course-e-02");
        }
        Category cat = categoryRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Category not found", "cat-e-01"));

        Course course = mapper.toEntity(dto);
        course.setCategory(cat);
        course = courseRepo.save(course);
        return mapper.toDto(course);
    }

    @Override
    public CourseDto update(String id, UpdateCourseRequest dto) {
        Course c = courseRepo.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Course not found", "course-e-01"));

        if (dto.getName() != null && !dto.getName().equals(c.getName())
                && courseRepo.existsByName(dto.getName())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Course name exists", "course-e-02");
        }
        if (dto.getCategoryId() != null) {
            Category cat = categoryRepo.findById(dto.getCategoryId())
                    .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Category not found", "cat-e-01"));
            c.setCategory(cat);
        }
        mapper.updateFromDto(dto, c);
        c = courseRepo.save(c);
        return mapper.toDto(c);
    }

    @Override
    public void delete(String id) {
        Course c = courseRepo.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Course not found", "course-e-01"));
        courseRepo.delete(c);
    }
}
