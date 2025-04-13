package com.short_term_course.mapper;

import com.short_term_course.dto.category.CategoryDto;
import com.short_term_course.dto.category.CreateCategoryRequest;
import com.short_term_course.dto.category.UpdateCategoryRequest;
import com.short_term_course.entities.Category;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    // Entity → DTO
    CategoryDto toDto(Category category);

    // CreateRequest → Entity (chỉ gán name)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "courses", ignore = true)
    Category toEntity(CreateCategoryRequest dto);

    // UpdateRequest → Entity (chỉ gán name)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "courses", ignore = true)
    void updateEntityFromDto(UpdateCategoryRequest dto, @MappingTarget Category category);
}
