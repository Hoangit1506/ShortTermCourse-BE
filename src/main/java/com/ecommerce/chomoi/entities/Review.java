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
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "rvw_id")
    String id;

    @Column(name = "rvw_rating", nullable = false)
    String rating;

    @Column(name = "rvw_comment", nullable = false)
    String comment;

    @Column(name = "rvw_response")
    String response;

    @Column(name = "rvw_image")
    String image;

    @Column(name = "rvw_video")
    String video;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prd_id")
    Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sku_id")
    SKU sku;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "odr_id")
    Order order;
}
