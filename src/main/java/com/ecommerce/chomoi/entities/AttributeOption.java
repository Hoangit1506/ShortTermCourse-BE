package com.ecommerce.chomoi.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class AttributeOption {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "att_opt_id")
    String id;

    @Column(name = "att_opt_value", nullable = false)
    String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "att_id")
    Attribute attribute;
}
