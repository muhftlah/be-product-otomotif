package com.mfathullah.be_product_otomotif.dto.response;

public record ModelResponse(
        Integer id,
        String name,
        Integer brandId,
        String brandName,
        boolean isActive
) {}
