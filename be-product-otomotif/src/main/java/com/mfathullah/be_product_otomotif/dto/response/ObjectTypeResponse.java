package com.mfathullah.be_product_otomotif.dto.response;

public record ObjectTypeResponse(
        Integer id,
        String name,
        Integer groupObjectId,
        String groupObjectName,
        boolean isActive
) {}