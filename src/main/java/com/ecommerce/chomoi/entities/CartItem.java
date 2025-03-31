package com.ecommerce.chomoi.entities;

import com.ecommerce.chomoi.entities.embeddedIds.CartItemId;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class CartItem {

    @EmbeddedId
    CartItemId id;

    @Column(name = "crt_itm_quantity")
    String quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crt_id")
    Cart cart;

    @ManyToOne
    @JoinColumn(name = "sku_id")
    SKU sku;
}
