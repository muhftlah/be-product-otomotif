package com.mfathullah.be_product_otomotif.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ModelRequest(
        @NotBlank String name,
        @NotNull Integer brandId
) {}
