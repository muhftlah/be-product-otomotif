package com.mfathullah.be_product_otomotif.model.params;

import com.mfathullah.be_product_otomotif.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "object_type")
@Getter @Setter
public class ObjectType extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_object_id", nullable = false)
    private GroupObject groupObject;
}
