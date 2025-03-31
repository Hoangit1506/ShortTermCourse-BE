package com.ecommerce.chomoi.entities;

import com.ecommerce.chomoi.entities.embeddedIds.ProductAttributeId;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class ProductAttribute {

    @EmbeddedId
    ProductAttributeId id;

    @Column(name = "prd_itm_value", nullable = false)
    String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prd_id")
    Product product;

    @ManyToOne
    @JoinColumn(name = "att_id")
    Attribute attribute;
}
