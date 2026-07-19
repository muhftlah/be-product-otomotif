package com.mfathullah.be_product_otomotif.controller;

import com.mfathullah.be_product_otomotif.dto.request.ObjectTypeRequest;
import com.mfathullah.be_product_otomotif.dto.response.ObjectTypeResponse;
import com.mfathullah.be_product_otomotif.service.ObjectTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/object-types")
@RequiredArgsConstructor
public class ObjectTypeController {

    private final ObjectTypeService objectTypeService;

    @GetMapping
    public ResponseEntity<List<ObjectTypeResponse>> findAll(
            @RequestParam(required = false) Integer groupObjectId) {
        if (groupObjectId != null) {
            return ResponseEntity.ok(objectTypeService.findByGroupObject(groupObjectId));
        }
        return ResponseEntity.ok(objectTypeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ObjectTypeResponse> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(objectTypeService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ObjectTypeResponse> create(@Valid @RequestBody ObjectTypeRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(objectTypeService.create(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ObjectTypeResponse> update(@PathVariable Integer id, @Valid @RequestBody ObjectTypeRequest req) {
        return ResponseEntity.ok(objectTypeService.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        objectTypeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
