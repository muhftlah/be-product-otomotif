package com.mfathullah.be_product_otomotif.model.params;

import com.mfathullah.be_product_otomotif.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "group_object")
@Getter
@Setter
public class GroupObject extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;
}
