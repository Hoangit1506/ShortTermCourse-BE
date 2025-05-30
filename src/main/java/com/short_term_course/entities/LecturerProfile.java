package com.short_term_course.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "lecturer_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LecturerProfile {

    @Id
    @Column(name = "acc_id")
    @JsonIgnore
    String accountId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "acc_id")
    Account account;

    @Column(name = "position")
    String position;

    @Column(name = "degree")
    String degree;

    @ManyToMany
    @JoinTable(
            name = "lecturer_specializations",
            joinColumns = @JoinColumn(name = "acc_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    @JsonIgnoreProperties({"courses","lecturers"})
    Set<Category> specializations = new HashSet<>();
}
