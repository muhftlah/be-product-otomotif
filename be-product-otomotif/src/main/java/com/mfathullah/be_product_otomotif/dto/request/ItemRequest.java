package com.mfathullah.be_product_otomotif.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ItemRequest(
        @NotBlank String code,
        @NotBlank String name,
        @NotNull Integer groupObjectId,
        @NotNull Integer objectTypeId,
        @NotNull Integer brandId,
        @NotNull Integer modelId,
        Boolean isActive
) {}
