package com.mfathullah.be_product_otomotif.dto.response;

public record StockResponse(
        Integer variantId,
        String sku,
        Integer quantityOnHand,
        Integer quantityReserved,
        Integer quantityAvailable
) {}
