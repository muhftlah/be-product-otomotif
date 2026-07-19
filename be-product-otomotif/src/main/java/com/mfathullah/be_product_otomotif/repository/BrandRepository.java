package com.mfathullah.be_product_otomotif.repository;

import com.mfathullah.be_product_otomotif.model.params.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Integer> {
    Optional<Brand> findByNameAndIsActiveTrue(String name);
    List<Brand> findAllByIsActiveTrue();
}