package com.mfathullah.be_product_otomotif.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record PurchaseRequest(
        @NotBlank String customerName,
        @NotEmpty @Valid List<PurchaseLineRequest> lines
) {}
