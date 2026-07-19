package com.mfathullah.be_product_otomotif.model.params;

import com.mfathullah.be_product_otomotif.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "model")
@Getter @Setter
public class Model extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;
}