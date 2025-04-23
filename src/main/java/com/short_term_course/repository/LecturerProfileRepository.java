package com.short_term_course.repository;

import com.short_term_course.entities.LecturerProfile;
import com.short_term_course.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LecturerProfileRepository extends JpaRepository<LecturerProfile,String> {
    @Modifying
    @Query(value = "DELETE FROM lecturer_specializations WHERE acc_id = :accId", nativeQuery = true)
    void deleteSpecializationsByAccId(@Param("accId") String accId);
}
