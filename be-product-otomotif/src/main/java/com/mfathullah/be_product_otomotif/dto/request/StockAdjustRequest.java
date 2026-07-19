package com.mfathullah.be_product_otomotif.dto.request;

import jakarta.validation.constraints.NotNull;

public record StockAdjustRequest(
        @NotNull Integer quantity,
        String reason
) {}
