package com.mfathullah.be_product_otomotif.dto.response;

import java.util.List;

public record ItemDetailResponse(
        Integer id,
        String code,
        String name,
        String groupObjectName,
        String objectTypeName,
        String brandName,
        String modelName,
        boolean isActive,
        List<VariantStockView> variants
) {}
