package com.ecommerce.chomoi.entities;

import com.ecommerce.chomoi.enums.Role;
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
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "shp_id")
    String id;

    @Column(name = "shp_name", nullable = false)
    String name;

    @Column(name = "shp_avatar")
    String avatar;

    @Column(name = "shp_cover_image")
    String cover_image;

    @Column(name = "shp_rating")
    Double rating;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    ShopStatus status;

    @OneToOne
    @JoinColumn(name = "acc_id")
    Account account;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "adr_id")
    Address address;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "shop", orphanRemoval = true, fetch = FetchType.LAZY)
    Set<Product> products = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "shop", orphanRemoval = true, fetch = FetchType.LAZY)
    Set<Order> orders = new HashSet<>();

    @PrePersist
    void onCreate() {
        this.status = ShopStatus.ACTIVE;
    }

}
