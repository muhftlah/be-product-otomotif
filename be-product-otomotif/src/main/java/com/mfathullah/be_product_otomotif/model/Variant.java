package com.mfathullah.be_product_otomotif.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "variant")
@Getter @Setter
public class Variant extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(nullable = false, unique = true)
    private String sku;

    @Column(nullable = false)
    private String name;

    private String color;
    private Integer engineCc;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal price;

    @OneToOne(mappedBy = "variant", cascade = CascadeType.ALL, orphanRemoval = true)
    private Stock stock;
}
