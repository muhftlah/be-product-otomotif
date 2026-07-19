package com.mfathullah.be_product_otomotif.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PurchaseLineRequest(
        @NotNull Integer variantId,
        @NotNull @Min(1) Integer quantity
) {}
