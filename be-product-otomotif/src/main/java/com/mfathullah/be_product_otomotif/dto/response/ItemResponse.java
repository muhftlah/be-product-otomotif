package com.mfathullah.be_product_otomotif.dto.response;

import java.time.LocalDateTime;

public record ItemResponse(
        Integer id,
        String code,
        String name,
        String groupObjectName,
        String objectTypeName,
        String brandName,
        String modelName,
        boolean isActive,
        LocalDateTime createdAt
) {}
