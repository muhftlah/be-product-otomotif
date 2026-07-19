package com.mfathullah.be_product_otomotif.controller;

import com.mfathullah.be_product_otomotif.dto.request.ItemRequest;
import com.mfathullah.be_product_otomotif.dto.response.ItemDetailResponse;
import com.mfathullah.be_product_otomotif.dto.response.ItemResponse;
import com.mfathullah.be_product_otomotif.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public ResponseEntity<List<ItemResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(itemService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponse> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(itemService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ItemResponse> create(@Valid @RequestBody ItemRequest req) {
        ItemResponse res = itemService.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemResponse> update(@PathVariable Integer id, @Valid @RequestBody ItemRequest req) {
        return ResponseEntity.ok(itemService.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        itemService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/detail")
    public ResponseEntity<ItemDetailResponse> findDetail(@PathVariable Integer id) {
        return ResponseEntity.ok(itemService.findDetailById(id));
    }
}
