package com.mfathullah.be_product_otomotif.service;

import com.mfathullah.be_product_otomotif.dto.request.PurchaseLineRequest;
import com.mfathullah.be_product_otomotif.dto.request.PurchaseRequest;
import com.mfathullah.be_product_otomotif.dto.response.PurchaseLineResult;
import com.mfathullah.be_product_otomotif.dto.response.PurchaseResponse;
import com.mfathullah.be_product_otomotif.exception.InsufficientStockException;
import com.mfathullah.be_product_otomotif.exception.ResourceNotFoundException;
import com.mfathullah.be_product_otomotif.model.Stock;
import com.mfathullah.be_product_otomotif.model.Variant;
import com.mfathullah.be_product_otomotif.repository.StockRepository;
import com.mfathullah.be_product_otomotif.repository.VariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final VariantRepository variantRepository;
    private final StockRepository stockRepository;

    @Transactional
    public PurchaseResponse purchase(PurchaseRequest req) {
        List<PurchaseLineResult> results = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (PurchaseLineRequest line : req.lines()) {
            Variant variant = variantRepository.findById(line.variantId())
                    .filter(Variant::isActive)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Variant id " + line.variantId() + " tidak ditemukan"));

            Stock stock = stockRepository.findByVariantIdForUpdate(variant.getId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Stock untuk variant " + variant.getId() + " tidak ditemukan"));

            int available = stock.getQuantityOnHand() - stock.getQuantityReserved();
            if (available < line.quantity()) {
                throw new InsufficientStockException(
                        "Stok tidak mencukupi untuk SKU '" + variant.getSku() +
                                "'. Tersedia: " + available + ", diminta: " + line.quantity());
            }

            stock.setQuantityOnHand(stock.getQuantityOnHand() - line.quantity());

            BigDecimal subtotal = variant.getPrice().multiply(BigDecimal.valueOf(line.quantity()));
            total = total.add(subtotal);

            results.add(new PurchaseLineResult(
                    variant.getId(), variant.getSku(), variant.getName(),
                    line.quantity(), variant.getPrice(), subtotal
            ));
        }

        return new PurchaseResponse(req.customerName(), results, total, LocalDateTime.now());
    }
}
