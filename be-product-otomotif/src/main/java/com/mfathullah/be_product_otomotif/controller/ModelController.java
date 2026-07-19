package com.mfathullah.be_product_otomotif.controller;

import com.mfathullah.be_product_otomotif.dto.request.ModelRequest;
import com.mfathullah.be_product_otomotif.dto.response.ModelResponse;
import com.mfathullah.be_product_otomotif.service.ModelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/models")
@RequiredArgsConstructor
public class ModelController {

    private final ModelService modelService;

    @GetMapping
    public ResponseEntity<List<ModelResponse>> findAll(
            @RequestParam(required = false) Integer brandId) {
        if (brandId != null) {
            return ResponseEntity.ok(modelService.findByBrand(brandId));
        }
        return ResponseEntity.ok(modelService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModelResponse> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(modelService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ModelResponse> create(@Valid @RequestBody ModelRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(modelService.create(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ModelResponse> update(@PathVariable Integer id, @Valid @RequestBody ModelRequest req) {
        return ResponseEntity.ok(modelService.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        modelService.delete(id);
        return ResponseEntity.noContent().build();
    }
}