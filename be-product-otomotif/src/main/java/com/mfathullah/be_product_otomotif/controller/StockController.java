package com.mfathullah.be_product_otomotif.controller;

import com.mfathullah.be_product_otomotif.dto.request.StockAdjustRequest;
import com.mfathullah.be_product_otomotif.dto.request.StockReserveRequest;
import com.mfathullah.be_product_otomotif.dto.response.StockResponse;
import com.mfathullah.be_product_otomotif.service.StockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/variants/{variantId}/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @GetMapping
    public ResponseEntity<StockResponse> get(@PathVariable Integer variantId) {
        return ResponseEntity.ok(stockService.findByVariantId(variantId));
    }

    @PatchMapping("/add")
    public ResponseEntity<StockResponse> add(@PathVariable Integer variantId,
                                             @Valid @RequestBody StockAdjustRequest req) {
        return ResponseEntity.ok(stockService.add(variantId, req));
    }

    @PatchMapping("/adjust")
    public ResponseEntity<StockResponse> adjust(@PathVariable Integer variantId,
                                             @Valid @RequestBody StockAdjustRequest req) {
        return ResponseEntity.ok(stockService.adjust(variantId, req));
    }

    @PostMapping("/reserve")
    public ResponseEntity<StockResponse> reserve(@PathVariable Integer variantId,
                                                 @Valid @RequestBody StockReserveRequest req) {
        return ResponseEntity.ok(stockService.reserve(variantId, req));
    }

    @PostMapping("/release")
    public ResponseEntity<StockResponse> release(@PathVariable Integer variantId,
                                                 @Valid @RequestBody StockReserveRequest req) {
        return ResponseEntity.ok(stockService.release(variantId, req));
    }

    @PostMapping("/reduction")
    public ResponseEntity<StockResponse> reduction(@PathVariable Integer variantId,
                                                @Valid @RequestBody StockReserveRequest req) {
        return ResponseEntity.ok(stockService.reduction(variantId, req));
    }
}