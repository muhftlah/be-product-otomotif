package com.mfathullah.be_product_otomotif.service;

import com.mfathullah.be_product_otomotif.dto.response.ItemDetailResponse;
import com.mfathullah.be_product_otomotif.dto.response.VariantStockView;
import com.mfathullah.be_product_otomotif.model.Item;
import com.mfathullah.be_product_otomotif.dto.request.ItemRequest;
import com.mfathullah.be_product_otomotif.dto.response.ItemResponse;
import com.mfathullah.be_product_otomotif.exception.ResourceNotFoundException;
import com.mfathullah.be_product_otomotif.exception.DuplicateResourceException;
import com.mfathullah.be_product_otomotif.model.Stock;
import com.mfathullah.be_product_otomotif.model.Variant;
import com.mfathullah.be_product_otomotif.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;
    private final GroupObjectRepository groupObjectRepository;
    private final ObjectTypeRepository objectTypeRepository;
    private final BrandRepository brandRepository;
    private final ModelRepository modelRepository;

    public List<ItemResponse> findAll(Pageable pageable) {
        return itemRepository.findAllByIsActiveTrue(pageable).map(this::toResponse).getContent();
    }

    public ItemResponse findById(Integer id) {
        return toResponse(getItemOrThrow(id));
    }

    @Transactional
    public ItemResponse create(ItemRequest req) {
        itemRepository.findByCodeAndIsActiveTrue(req.code()).ifPresent(i -> {
            throw new DuplicateResourceException("Item dengan code '" + req.code() + "' sudah ada");
        });

        Item item = new Item();
        item.setCode(req.code());
        item.setName(req.name());
        item.setGroupObject(groupObjectRepository.findById(req.groupObjectId())
                .orElseThrow(() -> new ResourceNotFoundException("GroupObject tidak ditemukan")));
        item.setObjectType(objectTypeRepository.findById(req.objectTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("ObjectType tidak ditemukan")));
        item.setBrand(brandRepository.findById(req.brandId())
                .orElseThrow(() -> new ResourceNotFoundException("Brand tidak ditemukan")));
        item.setModel(modelRepository.findById(req.modelId())
                .orElseThrow(() -> new ResourceNotFoundException("Model tidak ditemukan")));
        item.setActive(true);

        return toResponse(itemRepository.save(item));
    }

    @Transactional
    public ItemResponse update(Integer id, ItemRequest req) {
        Item item = getItemOrThrowNA(id);
        item.setGroupObject(groupObjectRepository.findById(req.groupObjectId())
                .orElseThrow(() -> new ResourceNotFoundException("GroupObject tidak ditemukan")));
        item.setObjectType(objectTypeRepository.findById(req.objectTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("ObjectType tidak ditemukan")));
        item.setBrand(brandRepository.findById(req.brandId())
                .orElseThrow(() -> new ResourceNotFoundException("Brand tidak ditemukan")));
        item.setModel(modelRepository.findById(req.modelId())
                .orElseThrow(() -> new ResourceNotFoundException("Model tidak ditemukan")));


        item.setCode(req.code());
        item.setName(req.name());
        item.setActive(req.isActive() != null ? req.isActive() : true);
        return toResponse(item);
    }

    @Transactional
    public void delete(Integer id) {
        getItemOrThrow(id).setActive(false);
    }

    private Item getItemOrThrow(Integer id) {
        return itemRepository.findById(id)
                .filter(Item::isActive)
                .orElseThrow(() -> new ResourceNotFoundException("Item id " + id + " tidak ditemukan"));
    }

    private Item getItemOrThrowNA(Integer id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item id " + id + " tidak ditemukan"));
    }

    private ItemResponse toResponse(Item i) {
        return new ItemResponse(
                i.getId(), i.getCode(), i.getName(),
                i.getGroupObject().getName(), i.getObjectType().getName(),
                i.getBrand().getName(), i.getModel().getName(),
                i.isActive(), i.getCreatedAt()
        );
    }

    public ItemDetailResponse findDetailById(Integer id) {
        Item item = getItemOrThrow(id);
        List<VariantStockView> variants = item.getVariants().stream()
                .filter(Variant::isActive)
                .map(v -> {
                    Stock s = v.getStock();
                    return new VariantStockView(
                            v.getId(), v.getSku(), v.getName(), v.getColor(), v.getEngineCc(),
                            v.getPrice(),
                            s != null ? s.getQuantityOnHand() : 0,
                            s != null ? s.getQuantityReserved() : 0,
                            s != null ? s.getQuantityAvailable() : 0
                    );
                }).toList();

        return new ItemDetailResponse(
                item.getId(), item.getCode(), item.getName(),
                item.getGroupObject().getName(), item.getObjectType().getName(),
                item.getBrand().getName(), item.getModel().getName(),
                item.isActive(), variants
        );
    }
}