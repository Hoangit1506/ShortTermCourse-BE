package com.short_term_course.mapper;

import com.short_term_course.dto.lecturer.*;
import com.short_term_course.entities.*;
import org.mapstruct.*;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface LecturerMapper {
    @Mapping(source="account.id",           target="id")
    @Mapping(source="account.email",        target="email")
    @Mapping(source="account.displayName",  target="displayName")
    @Mapping(source="account.phoneNumber",  target="phoneNumber")
    @Mapping(source="account.dob",          target="dob")
    @Mapping(source="account.avatar",       target="avatar")
    @Mapping(source="profile.position",     target="position")
    @Mapping(source="profile.degree",       target="degree")
    LecturerDto toDto(Account account, LecturerProfile profile);

    @Mapping(target="id", ignore=true)
    @Mapping(source="dto.email", target="email")
    @Mapping(source="dto.password", target="password")
    @Mapping(source="dto.displayName", target="displayName")
    @Mapping(source="dto.phoneNumber", target="phoneNumber")
    @Mapping(source="dto.avatar",      target="avatar")
    @Mapping(source="dto.dob",        target="dob")
    @Mapping(target="roles", ignore=true)
    @Mapping(target="isLocal", constant="true")
    Account toAccount(CreateLecturerRequest dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source="dto.position", target="position")
    @Mapping(source="dto.degree",   target="degree")
    @Mapping(target="specializations", ignore=true)
    void updateProfileFromDto(UpdateLecturerRequest dto, @MappingTarget LecturerProfile profile);
}
