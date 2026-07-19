package com.mfathullah.be_product_otomotif.repository;

import com.mfathullah.be_product_otomotif.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    Optional<Item> findByCodeAndIsActiveTrue(String code);
    Page<Item> findAllByIsActiveTrue(Pageable pageable);
}