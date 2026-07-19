package com.mfathullah.be_product_otomotif.dto.response;

import java.time.LocalDateTime;

public record GroupObjectResponse(
        Integer id,
        String name,
        boolean isActive,
        LocalDateTime createdAt
) {}
