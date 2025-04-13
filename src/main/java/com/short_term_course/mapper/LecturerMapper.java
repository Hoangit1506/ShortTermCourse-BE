package com.short_term_course.mapper;

import com.short_term_course.dto.lecturer.*;
import com.short_term_course.entities.*;
import org.mapstruct.*;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface LecturerMapper {
    // Map Account + Profile → DTO
    @Mapping(source="account.id",           target="id")
    @Mapping(source="account.email",        target="email")
    @Mapping(source="account.displayName",  target="displayName")
    @Mapping(source="account.phoneNumber",  target="phoneNumber")
    @Mapping(source="account.avatar",       target="avatar")
    @Mapping(source="profile.position",     target="position")
    @Mapping(source="profile.degree",       target="degree")
    // Explicit mapping cho specializationIds
//    @Mapping(source="profile.specializations",
//            target="specializationIds",
//            qualifiedByName="categoryIds")
//    // Explicit mapping cho specializationNames
//    @Mapping(source="profile.specializations",
//            target="specializationNames",
//            qualifiedByName="categoryNames")
    LecturerDto toDto(Account account, LecturerProfile profile);

    // method map Category → tên
//    @Named("categoryNames")
//    default Set<String> mapCategoryNames(Set<Category> cats) {
//        return cats.stream()
//                .map(Category::getName)
//                .collect(Collectors.toSet());
//    }
//
//    // method map Category → id
//    @Named("categoryIds")
//    default Set<String> mapCategoryIds(Set<Category> cats) {
//        return cats.stream()
//                .map(Category::getId)
//                .collect(Collectors.toSet());
//    }

    // Create Account từ CreateLecturerRequest
    @Mapping(target="id", ignore=true)
    @Mapping(source="dto.email", target="email")
    @Mapping(source="dto.password", target="password")
    @Mapping(source="dto.displayName", target="displayName")
    @Mapping(source="dto.phoneNumber", target="phoneNumber")
    @Mapping(source="dto.avatar",      target="avatar")
    @Mapping(target="roles", ignore=true)
    @Mapping(target="isLocal", constant="true")
    Account toAccount(CreateLecturerRequest dto);

    // Cập nhật LecturerProfile, bỏ qua specializations
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source="dto.position", target="position")
    @Mapping(source="dto.degree",   target="degree")
    @Mapping(target="specializations", ignore=true)
    void updateProfileFromDto(UpdateLecturerRequest dto, @MappingTarget LecturerProfile profile);
}
