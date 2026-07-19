package com.mfathullah.be_product_otomotif.model;

import com.mfathullah.be_product_otomotif.model.params.Brand;
import com.mfathullah.be_product_otomotif.model.params.GroupObject;
import com.mfathullah.be_product_otomotif.model.params.Model;
import com.mfathullah.be_product_otomotif.model.params.ObjectType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "item")
@Getter @Setter
public class Item extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String code;   // mis. "HONDA-BEAT"

    @Column(nullable = false)
    private String name;   // mis. "Honda Beat"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_object_id", nullable = false)
    private GroupObject groupObject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "object_type_id", nullable = false)
    private ObjectType objectType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id", nullable = false)
    private Model model;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Variant> variants = new ArrayList<>();
}