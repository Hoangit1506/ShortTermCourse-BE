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
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "adr_id")
    String id;

    @Column(name = "adr_province", nullable = false)
    String province;

    @Column(name = "adr_district", nullable = false)
    String district;

    @Column(name = "adr_ward", nullable = false)
    String ward;

    @Column(name = "adr_detail", nullable = false)
    String detail;

    @Column(name = "adr_receiver_name", nullable = false)
    String receiver_name;

    @Column(name = "adr_receiver_phone", nullable = false)
    String receiver_phone;

    @OneToOne(mappedBy = "address")
    Shop shop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acc_id")
    Account account;
}
