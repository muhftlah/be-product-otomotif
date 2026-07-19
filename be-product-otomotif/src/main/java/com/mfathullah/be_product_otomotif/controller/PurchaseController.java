package com.mfathullah.be_product_otomotif.controller;

import com.mfathullah.be_product_otomotif.dto.request.PurchaseRequest;
import com.mfathullah.be_product_otomotif.dto.response.PurchaseResponse;
import com.mfathullah.be_product_otomotif.service.PurchaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    @PostMapping
    public ResponseEntity<PurchaseResponse> purchase(@Valid @RequestBody PurchaseRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(purchaseService.purchase(req));
    }
}
