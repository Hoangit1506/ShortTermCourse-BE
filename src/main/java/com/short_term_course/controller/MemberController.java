package com.short_term_course.controller;

import com.short_term_course.dto.api.ApiResponse;
import com.short_term_course.dto.member.*;
import com.short_term_course.dto.pagination.PagedResponse;
import com.short_term_course.repository.ClassroomRepository;
import com.short_term_course.service.ClassroomService;
import com.short_term_course.service.MemberService;
import com.short_term_course.util.jwt.BaseJWTUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService service;

    // 1. Sinh viên xem lớp đã đăng ký
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/members")
    public ResponseEntity<ApiResponse<List<MemberDto>>> myEnrollments() {
        String studentId = BaseJWTUtil.getPayload().getId();
        var list = service.listByStudent(studentId);
        return ResponseEntity.ok(ApiResponse.<List<MemberDto>>builder().data(list).build());
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/members/my-courses")
    public ResponseEntity<ApiResponse<PagedResponse<MemberDto>>> myCourses(
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 10, sort = "courseName") Pageable pageable
    ) {
        String studentId = BaseJWTUtil.getPayload().getId();
        var result = service.getMyCourses(studentId, categoryId, keyword, pageable);
        return ResponseEntity.ok(ApiResponse.<PagedResponse<MemberDto>>builder()
                .data(result)
                .build());
    }


    // 2. Sinh viên enroll (USER)
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/members")
    public ResponseEntity<ApiResponse<MemberDto>> enroll(
            @RequestBody @Valid CreateMemberRequest dto) {
        String studentId = BaseJWTUtil.getPayload().getId();
        MemberDto m = service.enroll(studentId, dto);
        return ResponseEntity.status(201)
                .body(ApiResponse.<MemberDto>builder().data(m).build());
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/members/check")
    public ResponseEntity<ApiResponse<Map<String, Boolean>>> checkRegistered(
            @RequestParam String classroomId) {
        // Lấy studentId từ JWT
        String studentId = BaseJWTUtil.getPayload().getId();
        boolean registered = service.isRegistered(studentId, classroomId);
        return ResponseEntity.ok(ApiResponse.<Map<String, Boolean>>builder()
                .data(Map.of("registered", registered))
                .build());
    }

    // 3. Sinh viên hủy đăng ký
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/members/delete/{classroomId}")
    public ResponseEntity<ApiResponse<Void>> cancel(
            @PathVariable String classroomId) {
        String studentId = BaseJWTUtil.getPayload().getId();
        service.cancel(studentId, classroomId);
        return ResponseEntity.ok(ApiResponse.<Void>builder().build());
    }

    // 4. Lecturer/ADMIN xem danh sách sinh viên trong 1 lớp
    @PreAuthorize("hasAnyRole('LECTURER','ADMIN')")
    @GetMapping("/classrooms/{classroomId}/members")
    public ResponseEntity<ApiResponse<List<MemberDto>>> listByClassroom(
            @PathVariable String classroomId) {
        var list = service.listByClassroom(classroomId);
        return ResponseEntity.ok(ApiResponse.<List<MemberDto>>builder().data(list).build());
    }

    // 5. Lecturer/ADMIN chấm điểm, cập nhật status
    @PreAuthorize("hasAnyRole('LECTURER','ADMIN')")
    @PutMapping("/classrooms/{classroomId}/members/update/{studentId}")
    public ResponseEntity<ApiResponse<MemberDto>> updateMember(
            @PathVariable String classroomId,
            @PathVariable String studentId,
            @RequestBody UpdateMemberRequest dto) {
        MemberDto m = service.update(classroomId, studentId, dto);
        return ResponseEntity.ok(ApiResponse.<MemberDto>builder().data(m).build());
    }

    // 6. Lecturer/ADMIN remove 1 student
    @PreAuthorize("hasAnyRole('LECTURER','ADMIN')")
    @DeleteMapping("/classrooms/{classroomId}/members/delete/{studentId}")
    public ResponseEntity<ApiResponse<Void>> removeStudent(
            @PathVariable String classroomId,
            @PathVariable String studentId) {
        service.remove(classroomId, studentId);
        return ResponseEntity.ok(ApiResponse.<Void>builder().build());
    }


}


