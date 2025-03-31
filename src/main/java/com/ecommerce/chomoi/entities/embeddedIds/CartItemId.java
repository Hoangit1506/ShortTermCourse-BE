package com.ecommerce.chomoi.entities.embeddedIds;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemId {
    @Column(name = "crt_itm_crt_id")
    String cartId;

    @Column(name = "crt_itm_sku_id")
    String skuId;
}
