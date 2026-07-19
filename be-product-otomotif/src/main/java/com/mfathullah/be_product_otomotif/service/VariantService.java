package com.mfathullah.be_product_otomotif.service;

import com.mfathullah.be_product_otomotif.dto.request.VariantRequest;
import com.mfathullah.be_product_otomotif.dto.response.VariantResponse;
import com.mfathullah.be_product_otomotif.exception.DuplicateResourceException;
import com.mfathullah.be_product_otomotif.exception.ResourceNotFoundException;
import com.mfathullah.be_product_otomotif.model.Item;
import com.mfathullah.be_product_otomotif.model.Stock;
import com.mfathullah.be_product_otomotif.model.Variant;
import com.mfathullah.be_product_otomotif.repository.ItemRepository;
import com.mfathullah.be_product_otomotif.repository.StockRepository;
import com.mfathullah.be_product_otomotif.repository.VariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VariantService {

    private final VariantRepository variantRepository;
    private final ItemRepository itemRepository;
    private final StockRepository stockRepository;

    public List<VariantResponse> findByItemId(Integer itemId) {
        return variantRepository.findAllByItemIdAndIsActiveTrue(itemId)
                .stream().map(this::toResponse).toList();
    }

    public VariantResponse findById(Integer id) {
        return toResponse(getVariantOrThrow(id));
    }

    @Transactional
    public VariantResponse create(VariantRequest req) {
        variantRepository.findBySkuAndIsActiveTrue(req.sku()).ifPresent(v -> {
            throw new DuplicateResourceException("SKU '" + req.sku() + "' sudah digunakan");
        });

        Item item = itemRepository.findById(req.itemId())
                .orElseThrow(() -> new ResourceNotFoundException("Item id " + req.itemId() + " tidak ditemukan"));

        Variant variant = new Variant();
        variant.setItem(item);
        variant.setSku(req.sku());
        variant.setName(req.name());
        variant.setColor(req.color());
        variant.setEngineCc(req.engineCc());
        variant.setPrice(req.price());
        variant.setActive(true);
        variant = variantRepository.save(variant);

        Stock stock = new Stock();
        stock.setVariant(variant);
        stock.setQuantityOnHand(req.initialStock() != null ? req.initialStock() : 0);
        stock.setQuantityReserved(0);
        stockRepository.save(stock);
        variant.setStock(stock);

        return toResponse(variant);
    }

    @Transactional
    public VariantResponse update(Integer id, VariantRequest req) {
        Variant variant = getVariantOrThrow(id);
        variant.setName(req.name());
        variant.setColor(req.color());
        variant.setEngineCc(req.engineCc());
        variant.setPrice(req.price());
        return toResponse(variant);
    }

    @Transactional
    public void delete(Integer id) {
        getVariantOrThrow(id).setActive(false);
    }

    private Variant getVariantOrThrow(Integer id) {
        return variantRepository.findById(id)
                .filter(Variant::isActive)
                .orElseThrow(() -> new ResourceNotFoundException("Variant id " + id + " tidak ditemukan"));
    }

    private VariantResponse toResponse(Variant v) {
        Stock s = v.getStock();
        return new VariantResponse(
                v.getId(), v.getItem().getId(), v.getItem().getName(),
                v.getSku(), v.getName(), v.getColor(), v.getEngineCc(),
                v.getPrice(),
                s != null ? s.getQuantityOnHand() : 0,
                s != null ? s.getQuantityAvailable() : 0,
                v.isActive()
        );
    }
}