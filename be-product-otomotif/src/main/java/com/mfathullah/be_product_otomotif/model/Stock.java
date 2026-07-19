package com.mfathullah.be_product_otomotif.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "stock")
@Getter
@Setter
public class Stock {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id", nullable = false, unique = true)
    private Variant variant;

    @Column(nullable = false)
    private Integer quantityOnHand = 0;

    @Column(nullable = false)
    private Integer quantityReserved = 0;

    @Version
    private Long version;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Transient
    public Integer getQuantityAvailable() {
        return quantityOnHand - quantityReserved;
    }
}
