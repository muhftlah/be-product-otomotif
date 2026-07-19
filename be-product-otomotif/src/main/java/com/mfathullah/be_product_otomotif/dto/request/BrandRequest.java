package com.mfathullah.be_product_otomotif.dto.request;

import jakarta.validation.constraints.NotBlank;

public record BrandRequest(
        @NotBlank String name
) {}
