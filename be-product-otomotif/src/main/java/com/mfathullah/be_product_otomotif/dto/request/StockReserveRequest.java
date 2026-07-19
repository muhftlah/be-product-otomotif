package com.mfathullah.be_product_otomotif.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record StockReserveRequest(
        @NotNull @Min(1) Integer quantity
) {}
