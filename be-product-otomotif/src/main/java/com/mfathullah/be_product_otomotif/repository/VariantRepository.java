package com.mfathullah.be_product_otomotif.repository;

import com.mfathullah.be_product_otomotif.model.Variant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VariantRepository extends JpaRepository<Variant, Integer> {
    List<Variant> findAllByItemIdAndIsActiveTrue(Integer itemId);
    Optional<Variant> findBySkuAndIsActiveTrue(String sku);
}