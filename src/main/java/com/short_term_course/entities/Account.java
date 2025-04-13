package com.short_term_course.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.short_term_course.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "accounts")
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "acc_id")
    String id;

    @Column(name = "acc_email", nullable = false)
    String email;

    @Column(name = "acc_password")
    String password;

    @Column(name = "acc_display_name")
    String displayName;

    @Column(name = "acc_dob")
    LocalDate dob;

    @Column(name = "acc_avatar")
    String avatar;

    @Column(name = "acc_phone_number")
    String phoneNumber;

    @Column(name = "acc_provider_name")
    String providerName;

    @Column(name = "acc_provider_id")
    String providerId;

    @Column(name = "acc_is_local", nullable = false)
    Boolean isLocal;

    //Use Enum Role to make relationship between Account and Role
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "accounts_roles", joinColumns = @JoinColumn(name = "acc_id"))
    @Column(name = "role_name")
    Set<Role> roles = new HashSet<>();

//    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonIgnore
//    private LecturerProfile lecturerProfile;
}
