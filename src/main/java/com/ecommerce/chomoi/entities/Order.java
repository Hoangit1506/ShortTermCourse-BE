package com.ecommerce.chomoi.entities;

import com.ecommerce.chomoi.enums.OrderStatus;
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
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ord_id")
    String id;

    @Column(name = "ord_total_quantity", nullable = false)
    String totalQuantity;

    @Column(name = "ord_total_price", nullable = false)
    String totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(name = "ord_note")
    String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adr_id")
    Address address;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Review> reviews = new HashSet<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<OrderItem> orderItems = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "byr_id")
    Account buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shp_id")
    Shop shop;

    @PrePersist
    protected void onCreate() {
        this.status = OrderStatus.PENDING;
    }
}
