package com.mfathullah.be_product_otomotif.repository;

import com.mfathullah.be_product_otomotif.model.params.Model;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ModelRepository extends JpaRepository<Model, Integer> {
    Optional<Model> findByNameAndIsActiveTrue(String name);
    List<Model> findAllByIsActiveTrue();
    List<Model> findAllByBrandIdAndIsActiveTrue(Integer brandId);
}