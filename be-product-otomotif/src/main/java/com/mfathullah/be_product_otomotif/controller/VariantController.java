package com.mfathullah.be_product_otomotif.controller;


import com.mfathullah.be_product_otomotif.dto.request.VariantRequest;
import com.mfathullah.be_product_otomotif.dto.response.VariantResponse;
import com.mfathullah.be_product_otomotif.service.VariantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class VariantController {

    private final VariantService variantService;

    @GetMapping("/api/items/{itemId}/variants")
    public ResponseEntity<List<VariantResponse>> findByItem(@PathVariable Integer itemId) {
        return ResponseEntity.ok(variantService.findByItemId(itemId));
    }

    @GetMapping("/api/variants/{id}")
    public ResponseEntity<VariantResponse> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(variantService.findById(id));
    }

    @PostMapping("/api/variants")
    public ResponseEntity<VariantResponse> create(@Valid @RequestBody VariantRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(variantService.create(req));
    }

    @PutMapping("/api/variants/{id}")
    public ResponseEntity<VariantResponse> update(@PathVariable Integer id, @Valid @RequestBody VariantRequest req) {
        return ResponseEntity.ok(variantService.update(id, req));
    }

    @DeleteMapping("/api/variants/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        variantService.delete(id);
        return ResponseEntity.noContent().build();
    }
}