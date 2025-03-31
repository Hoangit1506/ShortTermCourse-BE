package com.ecommerce.chomoi.entities;

import com.ecommerce.chomoi.enums.ProductStatus;
import com.ecommerce.chomoi.enums.ShopStatus;
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
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "prd_id")
    String id;

    @Column(name = "prd_name", nullable = false)
    String name;

    @Column(name = "prd_description", nullable = false)
    String description;

    @Column(name = "prd_rating")
    Double rating;

    @Column(name = "prd_sold")
    Integer sold;

    @Column(name = "prd_thumbnail", nullable = false)
    String thumbnail;

    @Column(name = "prd_video")
    String video;

    @Column(name = "prd_slug", nullable = false)
    String slug;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    ProductStatus status;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product", orphanRemoval = true)
    Set<Variation> variations = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product",orphanRemoval = true)
    Set<Image> images = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product", orphanRemoval = true)
    Set<SKU> skus = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product", orphanRemoval = true)
    Set<Review> reviews = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product", orphanRemoval = true)
    Set<ProductAttribute> productAttributes = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ctg_id")
    Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shp_id")
    Shop shop;

    @PrePersist
    void onCreate() {
        this.status = ProductStatus.PENDING;
        this.rating = 0.0;
        this.sold = 0;
    }
}
