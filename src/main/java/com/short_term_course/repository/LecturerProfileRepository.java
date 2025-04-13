package com.short_term_course.repository;

import com.short_term_course.entities.LecturerProfile;
import com.short_term_course.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LecturerProfileRepository extends JpaRepository<LecturerProfile,String> {

}
