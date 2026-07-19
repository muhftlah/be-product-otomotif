package com.mfathullah.be_product_otomotif.controller;

import com.mfathullah.be_product_otomotif.dto.request.GroupObjectRequest;
import com.mfathullah.be_product_otomotif.dto.response.GroupObjectResponse;
import com.mfathullah.be_product_otomotif.service.GroupObjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/group-objects")
@RequiredArgsConstructor
public class GroupObjectController {

    private final GroupObjectService groupObjectService;

    @GetMapping
    public ResponseEntity<List<GroupObjectResponse>> findAll() {
        return ResponseEntity.ok(groupObjectService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupObjectResponse> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(groupObjectService.findById(id));
    }

    @PostMapping
    public ResponseEntity<GroupObjectResponse> create(@Valid @RequestBody GroupObjectRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(groupObjectService.create(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupObjectResponse> update(@PathVariable Integer id, @Valid @RequestBody GroupObjectRequest req) {
        return ResponseEntity.ok(groupObjectService.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        groupObjectService.delete(id);
        return ResponseEntity.noContent().build();
    }
}