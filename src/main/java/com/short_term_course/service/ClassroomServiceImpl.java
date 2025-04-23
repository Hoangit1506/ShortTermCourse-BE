package com.short_term_course.service;

import com.short_term_course.dto.classroom.ClassroomDto;
import com.short_term_course.dto.classroom.CreateClassroomRequest;
import com.short_term_course.dto.classroom.UpdateClassroomRequest;
import com.short_term_course.dto.pagination.PagedResponse;
import com.short_term_course.entities.Account;
import com.short_term_course.entities.Classroom;
import com.short_term_course.entities.Course;
import com.short_term_course.exception.AppException;
import com.short_term_course.mapper.ClassroomMapper;
import com.short_term_course.repository.AccountRepository;
import com.short_term_course.repository.ClassroomRepository;
import com.short_term_course.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ClassroomServiceImpl implements ClassroomService {
    private final ClassroomRepository classroomRepo;
    private final CourseRepository courseRepo;
    private final AccountRepository accountRepo;
    private final ClassroomMapper mapper;

    @Override
    public PagedResponse<ClassroomDto> listAdmin(String keyword, String categoryId, String courseId, String lecturerId, Pageable pageable) {
        // 1️⃣ Lấy toàn bộ (unpaged) để lọc thủ công
        List<Classroom> all = classroomRepo.findAll();

        // 2️⃣ Lọc theo keyword (tên lớp)
        Stream<Classroom> stream = all.stream();
        if (keyword != null && !keyword.isBlank()) {
            String kw = keyword.toLowerCase();
            stream = stream.filter(c -> c.getName() != null
                    && c.getName().toLowerCase().contains(kw));
        }

        // 3️⃣ Lọc theo chuyên ngành của khóa học
        if (categoryId != null && !categoryId.isBlank()) {
            stream = stream.filter(c -> c.getCourse().getCategory().getId().equals(categoryId));
        }

        // 4️⃣ Lọc theo khóa học
        if (courseId != null && !courseId.isBlank()) {
            stream = stream.filter(c -> c.getCourse().getId().equals(courseId));
        }

        // 5️⃣ Lọc theo giảng viên
        if (lecturerId != null && !lecturerId.isBlank()) {
            stream = stream.filter(c -> c.getLecturer().getId().equals(lecturerId));
        }

        List<Classroom> filtered = stream.collect(Collectors.toList());

        //  ➡️  Sort lớp theo startDate gần nhất với ngày hôm nay, rồi theo tên
        LocalDate today = LocalDate.now();
        filtered.sort(Comparator
                .comparingLong((Classroom c) ->
                        Math.abs(ChronoUnit.DAYS.between(today, c.getStartDate()))
                )
                .thenComparing(Classroom::getName)
        );

        // 6️⃣ Phân trang thủ công
        int total = filtered.size();
        int pageNum  = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        int start    = pageNum * pageSize;
        int end      = Math.min(start + pageSize, total);

        List<Classroom> paged = (start <= end)
                ? filtered.subList(start, end)
                : List.of();

        // 7️⃣ Map sang DTO
        List<ClassroomDto> dtos = paged.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        // 8️⃣ Trả về PagedResponse
        return PagedResponse.<ClassroomDto>builder()
                .content(dtos)
                .pageNumber(pageNum)
                .pageSize(pageSize)
                .totalElements((long) total)
                .totalPages((int) Math.ceil((double) total / pageSize))
                .last(end == total)
                .build();
    }

    @Override
    @Transactional
    public ClassroomDto create(CreateClassroomRequest dto) {
        Course course = courseRepo.findById(dto.getCourseId())
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Course not found"));
        Account lecturer = accountRepo.findById(dto.getLecturerId())
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Lecturer not found"));

        Classroom c = Classroom.builder()
                .name(dto.getName())
                .course(course)
                .lecturer(lecturer)
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .place(dto.getPlace())
                .capacity(dto.getCapacity())
                .enrolled(0)
                .build();

        classroomRepo.save(c);
        return mapper.toDto(c);
    }

    @Override
    @Transactional
    public ClassroomDto update(String id, UpdateClassroomRequest dto) {
        Classroom c = classroomRepo.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Classroom not found"));

        if (dto.getName() != null)        c.setName(dto.getName());
        if (dto.getLecturerId() != null) {
            Account lec = accountRepo.findById(dto.getLecturerId())
                    .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Lecturer not found"));
            c.setLecturer(lec);
        }
        if (dto.getStartDate() != null)   c.setStartDate(dto.getStartDate());
        if (dto.getEndDate() != null)     c.setEndDate(dto.getEndDate());
        if (dto.getPlace() != null)       c.setPlace(dto.getPlace());
        if (dto.getCapacity() != null)    c.setCapacity(dto.getCapacity());

        classroomRepo.save(c);
        return mapper.toDto(c);
    }

    @Override
    @Transactional(readOnly = true)
    public ClassroomDto getById(String id) {
        Classroom c = classroomRepo.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Classroom not found"));
        return mapper.toDto(c);
    }

    @Override
    @Transactional
    public void delete(String id) {
        if (!classroomRepo.existsById(id)) {
            throw new AppException(HttpStatus.NOT_FOUND, "Classroom not found");
        }
        classroomRepo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<ClassroomDto> list(Pageable pageable) {
        Page<Classroom> page = classroomRepo.findAll(pageable);
        var dtos = page.map(mapper::toDto);
        return PagedResponse.<ClassroomDto>builder()
                .content(dtos.getContent())
                .pageNumber(dtos.getNumber())
                .pageSize(dtos.getSize())
                .totalElements(dtos.getTotalElements())
                .totalPages(dtos.getTotalPages())
                .last(dtos.isLast())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<ClassroomDto> filterOpenCourses(String keyword, String courseId,
                                                         String categoryId, String startDate, String endDate, Pageable pageable) {

        LocalDate start = (startDate == null || startDate.isBlank()) ? null : LocalDate.parse(startDate);
        LocalDate end   = (endDate   == null || endDate.isBlank())   ? null : LocalDate.parse(endDate);

        String kw = (keyword == null || keyword.isBlank()) ? null : keyword.toLowerCase();

        Page<Classroom> page = classroomRepo.filterOpenCourses(
                kw, courseId, categoryId, start, end, pageable
        );

        List<ClassroomDto> dtoList = page.getContent().stream()
                .map(mapper::toDto)
                .toList();

        return new PagedResponse<>(dtoList, pageable, page.getTotalElements());
    }

}
