package com.mfathullah.be_product_otomotif.controller;

import com.mfathullah.be_product_otomotif.dto.request.BrandRequest;
import com.mfathullah.be_product_otomotif.dto.response.BrandResponse;
import com.mfathullah.be_product_otomotif.service.BrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @GetMapping
    public ResponseEntity<List<BrandResponse>> findAll() {
        return ResponseEntity.ok(brandService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrandResponse> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(brandService.findById(id));
    }

    @PostMapping
    public ResponseEntity<BrandResponse> create(@Valid @RequestBody BrandRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(brandService.create(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BrandResponse> update(@PathVariable Integer id, @Valid @RequestBody BrandRequest req) {
        return ResponseEntity.ok(brandService.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        brandService.delete(id);
        return ResponseEntity.noContent().build();
    }
}