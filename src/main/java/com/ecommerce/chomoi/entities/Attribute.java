package com.ecommerce.chomoi.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Attribute {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "att_id")
    String id;

    @Column(name = "att_name", nullable = false)
    String name;

    @Column(name = "att_is_enter_by_hand")
    Boolean isEnterByHand = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ctg_id")
    Category category;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "attribute",orphanRemoval = true)
    Set<AttributeOption> options = new HashSet<>();
}
