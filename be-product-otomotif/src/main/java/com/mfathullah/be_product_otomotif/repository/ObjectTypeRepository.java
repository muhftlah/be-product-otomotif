package com.mfathullah.be_product_otomotif.repository;

import com.mfathullah.be_product_otomotif.model.params.ObjectType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ObjectTypeRepository extends JpaRepository<ObjectType, Integer> {
    Optional<ObjectType> findByNameAndIsActiveTrue(String name);
    List<ObjectType> findAllByIsActiveTrue();
    List<ObjectType> findAllByGroupObjectIdAndIsActiveTrue(Integer groupObjectId);
}