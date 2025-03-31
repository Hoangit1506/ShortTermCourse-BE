package com.ecommerce.chomoi.entities;

import com.ecommerce.chomoi.enums.SKUStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

// Them truong sold
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class SKU {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "sku_id")
    String id;

    @Column(name = "sku_price", nullable = false)
    String price;

    @Column(name = "sku_stock", nullable = false)
    String stock;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SKUStatus status;

    @Column(name = "sku_weight", nullable = false)
    String weight;

    @Column(name = "sku_is_default", nullable = false)
    Boolean isDefault;

    @Column(name = "sku_slug", nullable = false)
    String slug;

    @Column(name = "sku_variation", nullable = false)
    String variation;

    @Column(name = "sku_image", nullable = false)
    String image;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sku", orphanRemoval = true)
    Set<Review> reviews = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sku", orphanRemoval = true)
    Set<CartItem> cartItems = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sku", orphanRemoval = true)
    Set<OrderItem> orderItems = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prd_id")
    Product product;

    @PrePersist
    protected void onCreate() {
        this.status = SKUStatus.PENDING;
        this.isDefault = false;
    }
}
