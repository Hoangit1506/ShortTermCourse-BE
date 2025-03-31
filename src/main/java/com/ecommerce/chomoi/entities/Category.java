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
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ctg_id")
    String id;

    @Column(name = "ctg_name", nullable = false)
    String name;

    @Column(name = "ctg_parent_id")
    String parent_id;

    @Column(name = "ctg_left", nullable = false)
    Integer left;

    @Column(name = "ctg_right", nullable = false)
    Integer right;

    @Column(name = "ctg_level", nullable = false)
    Integer level;

    @Column(name = "ctg_is_leaf", nullable = false)
    Boolean isLeaf = true;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "category", orphanRemoval = true)
    Set<Attribute> attributes = new HashSet<>();
}
