package com.short_term_course.service;

import com.short_term_course.dto.member.*;
import com.short_term_course.entities.*;
import com.short_term_course.enums.*;
import com.short_term_course.exception.AppException;
import com.short_term_course.mapper.MemberMapper;
import com.short_term_course.repository.*;
import com.short_term_course.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        // 1. Lấy classroom
        Classroom c = classroomRepo.findById(dto.getClassroomId())
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Classroom not found"));
        // 2. Kiểm tra capacity
        if (c.getEnrolled() >= c.getCapacity()) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Classroom is full");
        }
        // 3. Kiểm tra đã đăng ký chưa
        MemberId mid = new MemberId(studentId, dto.getClassroomId());
        if (memberRepo.existsById(mid)) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Already enrolled");
        }
        // 4. Tạo Member
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
        // 5. Tăng enrolled
        c.setEnrolled(c.getEnrolled() + 1);
        classroomRepo.save(c);
        return mapper.toDto(m);
    }

    @Override
    public List<MemberDto> listByClassroom(String classroomId) {
        // Lecturer/ADMIN only
        return mapper.toDtoList(memberRepo.findByClassroomId(classroomId));
    }

    @Override
    public List<MemberDto> listByStudent(String studentId) {
        // USER xem lớp đã đăng ký
        return mapper.toDtoList(memberRepo.findByStudentId(studentId));
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
        // Giảm enrolled
        Classroom c = m.getClassroom();
        c.setEnrolled(c.getEnrolled() - 1);
        classroomRepo.save(c);
        // Xóa đăng ký
        memberRepo.delete(m);
    }

    @Override
    @Transactional
    public void remove(String classroomId, String studentId) {
        // Lecturer/ADMIN remove student
        cancel(studentId, classroomId);
    }
}
