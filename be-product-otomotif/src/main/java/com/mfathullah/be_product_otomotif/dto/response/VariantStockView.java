package com.mfathullah.be_product_otomotif.dto.response;

import java.math.BigDecimal;

public record VariantStockView(
        Integer variantId,
        String sku,
        String name,
        String color,
        Integer engineCc,
        BigDecimal price,
        Integer quantityOnHand,
        Integer quantityReserved,
        Integer quantityAvailable
) {}
