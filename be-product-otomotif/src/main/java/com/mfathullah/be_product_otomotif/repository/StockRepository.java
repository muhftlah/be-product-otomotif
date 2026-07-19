package com.mfathullah.be_product_otomotif.repository;


import com.mfathullah.be_product_otomotif.model.Stock;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Integer> {
    Optional<Stock> findByVariantId(Integer variantId);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Stock s WHERE s.variant.id = :variantId")
    Optional<Stock> findByVariantIdForUpdate(@Param("variantId") Integer variantId);
}
