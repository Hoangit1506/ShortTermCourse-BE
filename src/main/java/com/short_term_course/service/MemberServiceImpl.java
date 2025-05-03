package com.short_term_course.service;

import com.short_term_course.dto.member.*;
import com.short_term_course.dto.pagination.PagedResponse;
import com.short_term_course.entities.*;
import com.short_term_course.enums.*;
import com.short_term_course.exception.AppException;
import com.short_term_course.mapper.MemberMapper;
import com.short_term_course.repository.*;
import com.short_term_course.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepo;
    private final ClassroomRepository classroomRepo;
    private final AccountRepository accountRepo;
    private final MemberMapper mapper;
    private final SecurityUtil securityUtil;

    @Override
    @Transactional
    public MemberDto enroll(String studentId, CreateMemberRequest dto) {
        Classroom c = classroomRepo.findById(dto.getClassroomId())
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Classroom not found"));
        if (LocalDate.now().isAfter(c.getStartDate())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Đã quá hạn đăng ký");
        }
        if (c.getEnrolled() >= c.getCapacity()) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Classroom is full");
        }
        MemberId mid = new MemberId(studentId, dto.getClassroomId());
        if (memberRepo.existsById(mid)) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Already enrolled");
        }
        Account stu = accountRepo.findById(studentId)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Student not found"));
        Member m = Member.builder()
                .id(mid)
                .student(stu)
                .classroom(c)
                .score(null)
                .learningStatus(LearningStatus.REGISTERED)
                .tuitionStatus(TuitionStatus.UNPAID)
                .build();
        memberRepo.save(m);
        c.setEnrolled(c.getEnrolled() + 1);
        classroomRepo.save(c);
        return mapper.toDto(m);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isRegistered(String studentId, String classroomId) {
        MemberId mid = new MemberId(studentId, classroomId);
        return memberRepo.existsById(mid);
    }

    @Override
    public List<MemberDto> listByClassroom(String classroomId) {
        return mapper.toDtoList(memberRepo.findByClassroomId(classroomId));
    }

    @Override
    public List<MemberDto> listByStudent(String studentId) {
        return mapper.toDtoList(memberRepo.findByStudentId(studentId));
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<MemberDto> getMyCourses(String studentId, String keyword, Pageable pageable) {
        Page<Member> page = memberRepo.findMyCourses(studentId, keyword, pageable);
        List<Member> sortedContent = page.getContent().stream()
                .sorted(Comparator
                        .comparingLong((Member m) -> Math.abs(ChronoUnit.DAYS.between(LocalDate.now(), m.getClassroom().getStartDate())))
                        .thenComparing(m -> m.getClassroom().getName()))
                .toList();
        List<MemberDto> dtos = sortedContent.stream().map(mapper::toDto).toList();
        return new PagedResponse<>(
                dtos,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }

    @Override
    @Transactional
    public MemberDto update(String classroomId, String studentId, UpdateMemberRequest dto) {
        MemberId mid = new MemberId(studentId, classroomId);
        Member m = memberRepo.findById(mid)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Enrollment not found"));
        if (dto.getScore() != null)           m.setScore(dto.getScore());
        if (dto.getLearningStatus() != null)  m.setLearningStatus(dto.getLearningStatus());
        if (dto.getTuitionStatus() != null)   m.setTuitionStatus(dto.getTuitionStatus());
        memberRepo.save(m);
        return mapper.toDto(m);
    }

    @Override
    @Transactional
    public void cancel(String studentId, String classroomId) {
        MemberId mid = new MemberId(studentId, classroomId);
        Member m = memberRepo.findById(mid)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Enrollment not found"));
        Classroom c = m.getClassroom();
        c.setEnrolled(c.getEnrolled() - 1);
        classroomRepo.save(c);
        memberRepo.delete(m);
    }

    @Override
    @Transactional
    public void remove(String classroomId, String studentId) {
        cancel(studentId, classroomId);
    }


}
