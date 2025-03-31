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
public class VariationOption {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "var_opt_id")
    String id;

    @Column(name = "var_opt_value")
    String optionValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "var_id")
    Variation variation;
}
