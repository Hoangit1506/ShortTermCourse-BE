package com.short_term_course.service;

import com.short_term_course.dto.lecturer.CreateLecturerRequest;
import com.short_term_course.dto.lecturer.LecturerDto;
import com.short_term_course.dto.lecturer.UpdateLecturerRequest;
import com.short_term_course.dto.pagination.PagedResponse;
import com.short_term_course.entities.Account;
import com.short_term_course.entities.Category;
import com.short_term_course.entities.LecturerProfile;
import com.short_term_course.enums.Role;
import com.short_term_course.exception.AppException;
import com.short_term_course.mapper.LecturerMapper;
import com.short_term_course.repository.AccountRepository;
import com.short_term_course.repository.CategoryRepository;
import com.short_term_course.repository.LecturerProfileRepository;
import com.short_term_course.repository.RefreshTokenRepository;
import com.short_term_course.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LecturerServiceImpl implements LecturerService {
    private final AccountRepository accountRepo;
    private final LecturerProfileRepository profileRepo;
    private final CategoryRepository categoryRepo;
    private final LecturerMapper mapper;
    private final PasswordUtil passwordUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public PagedResponse<LecturerDto> list(Pageable pageable) {
        Page<Account> page = accountRepo.findByRolesContains(Role.LECTURER, pageable);

        List<LecturerDto> dtos = page.stream().map(acc -> {
            // 1. Lấy profile (chỉ để map position/degree)
            LecturerProfile prof = profileRepo.findById(acc.getId())
                    .orElse(new LecturerProfile());

            // 2. Map các field cơ bản
            LecturerDto dto = mapper.toDto(acc, prof);

            // 3. Thủ công load chuyên ngành
            List<Category> cats = categoryRepo.findByLecturers_Account_Id(acc.getId());
            dto.setSpecializationIds(
                    cats.stream().map(Category::getId).collect(Collectors.toSet()));
            dto.setSpecializationNames(
                    cats.stream().map(Category::getName).collect(Collectors.toSet()));

            return dto;
        }).collect(Collectors.toList());

        return PagedResponse.<LecturerDto>builder()
                .content(dtos)
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    @Override
    public LecturerDto getById(String id) {
        Account acc = accountRepo.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Lecturer not found"));

        LecturerProfile prof = profileRepo.findById(id)
                .orElse(new LecturerProfile());

        LecturerDto dto = mapper.toDto(acc, prof);

        // Thủ công load chuyên ngành
        List<Category> cats = categoryRepo.findByLecturers_Account_Id(id);
        dto.setSpecializationIds(
                cats.stream().map(Category::getId).collect(Collectors.toSet()));
        dto.setSpecializationNames(
                cats.stream().map(Category::getName).collect(Collectors.toSet()));

        return dto;
    }


    @Override
    @Transactional
    public LecturerDto create(CreateLecturerRequest dto) {
        if (accountRepo.existsByEmailAndIsLocalTrue(dto.getEmail())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Email already exists");
        }

        Account acc = mapper.toAccount(dto);
        acc.setPassword(passwordUtil.encodePassword(dto.getPassword()));
        acc.setRoles(Set.of(Role.LECTURER, Role.USER));
        accountRepo.save(acc);

        // profile có specializations luôn được khởi tạo
        LecturerProfile prof = LecturerProfile.builder()
                .account(acc)
                .position(dto.getPosition())
                .degree(dto.getDegree())
                .build();

        Set<Category> cats = categoryRepo.findAllById(dto.getSpecializationIds()).stream().collect(Collectors.toSet());;
        prof.setSpecializations(cats);
        profileRepo.save(prof);

        // map thủ công specialization cho DTO
        LecturerDto result = mapper.toDto(acc, prof);
        result.setSpecializationIds(cats.stream().map(Category::getId).collect(Collectors.toSet()));
        result.setSpecializationNames(cats.stream().map(Category::getName).collect(Collectors.toSet()));
        return result;
    }

    @Override
    @Transactional
    public LecturerDto update(String id, UpdateLecturerRequest dto) {
        Account acc = accountRepo.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Lecturer not found"));
        LecturerProfile prof = profileRepo.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Profile not found"));

        if (dto.getDisplayName() != null) acc.setDisplayName(dto.getDisplayName());
        if (dto.getPhoneNumber() != null) acc.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getAvatar() != null) acc.setAvatar(dto.getAvatar());
        accountRepo.save(acc);

        // Update position/degree
        mapper.updateProfileFromDto(dto, prof);

        if (dto.getSpecializationIds() != null) {
            Set<Category> cats = categoryRepo.findAllById(dto.getSpecializationIds())
                    .stream().collect(Collectors.toSet());
            prof.setSpecializations(cats);
        }

        profileRepo.save(prof);

        // map thủ công chuyên ngành
        Set<Category> cats = categoryRepo.findByLecturers_Account_Id(id).stream().collect(Collectors.toSet());
        LecturerDto result = mapper.toDto(acc, prof);
        result.setSpecializationIds(cats.stream().map(Category::getId).collect(Collectors.toSet()));
        result.setSpecializationNames(cats.stream().map(Category::getName).collect(Collectors.toSet()));
        return result;
    }

    @Override
    @Transactional
    public void delete(String id) {
        if (!accountRepo.existsById(id)) {
            throw new AppException(HttpStatus.NOT_FOUND, "Lecturer not found");
        }
        refreshTokenRepository.findByAccountId(id).ifPresent(rt -> refreshTokenRepository.delete(rt));
        profileRepo.deleteById(id);
        accountRepo.deleteById(id);
    }
}
