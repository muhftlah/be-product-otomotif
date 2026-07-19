package com.mfathullah.be_product_otomotif.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record VariantRequest(
        @NotNull Integer itemId,
        @NotBlank String sku,
        @NotBlank String name,
        String color,
        Integer engineCc,
        @NotNull @DecimalMin("0.0") BigDecimal price,
        @Min(0) Integer initialStock
) {}
