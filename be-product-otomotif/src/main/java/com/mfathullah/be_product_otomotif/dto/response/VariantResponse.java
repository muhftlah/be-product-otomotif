package com.mfathullah.be_product_otomotif.dto.response;

import java.math.BigDecimal;

public record VariantResponse(
        Integer id, Integer itemId, String itemName,
        String sku, String name, String color, Integer engineCc,
        BigDecimal price, Integer quantityOnHand, Integer quantityAvailable,
        boolean isActive
) {}
