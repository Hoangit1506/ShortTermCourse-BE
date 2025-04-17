package com.short_term_course.repository;

import com.short_term_course.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    boolean existsByName(String name);
    List<Category> findByLecturers_Account_Id(String accountId);
    Page<Category> findByNameContainingIgnoreCase(String search, Pageable pageable);
}
