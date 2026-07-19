package com.mfathullah.be_product_otomotif.dto.response;

import java.math.BigDecimal;

public record PurchaseLineResult(
        Integer variantId,
        String sku,
        String variantName,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal subtotal
) {}
