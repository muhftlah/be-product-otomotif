package com.mfathullah.be_product_otomotif.repository;

import com.mfathullah.be_product_otomotif.model.params.GroupObject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupObjectRepository extends JpaRepository<GroupObject, Integer> {
    Optional<GroupObject> findByNameAndIsActiveTrue(String name);
    List<GroupObject> findAllByIsActiveTrue();
}