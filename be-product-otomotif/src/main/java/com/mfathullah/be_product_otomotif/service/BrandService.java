package com.mfathullah.be_product_otomotif.service;

import com.mfathullah.be_product_otomotif.dto.request.BrandRequest;
import com.mfathullah.be_product_otomotif.dto.response.BrandResponse;
import com.mfathullah.be_product_otomotif.exception.DuplicateResourceException;
import com.mfathullah.be_product_otomotif.exception.ResourceNotFoundException;
import com.mfathullah.be_product_otomotif.model.params.Brand;
import com.mfathullah.be_product_otomotif.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BrandService {

    private final BrandRepository brandRepository;

    public List<BrandResponse> findAll() {
        return brandRepository.findAllByIsActiveTrue()
                .stream().map(this::toResponse).toList();
    }

    public BrandResponse findById(Integer id) {
        return toResponse(getOrThrow(id));
    }

    @Transactional
    public BrandResponse create(BrandRequest req) {
        brandRepository.findByNameAndIsActiveTrue(req.name()).ifPresent(b -> {
            throw new DuplicateResourceException("Brand '" + req.name() + "' sudah ada");
        });
        Brand entity = new Brand();
        entity.setName(req.name());
        entity.setActive(true);
        return toResponse(brandRepository.save(entity));
    }

    @Transactional
    public BrandResponse update(Integer id, BrandRequest req) {
        Brand entity = getOrThrow(id);
        entity.setName(req.name());
        return toResponse(entity);
    }

    @Transactional
    public void delete(Integer id) {
        getOrThrow(id).setActive(false);
    }

    private Brand getOrThrow(Integer id) {
        return brandRepository.findById(id)
                .filter(Brand::isActive)
                .orElseThrow(() -> new ResourceNotFoundException("Brand id " + id + " tidak ditemukan"));
    }

    private BrandResponse toResponse(Brand b) {
        return new BrandResponse(b.getId(), b.getName(), b.isActive(), b.getCreatedAt());
    }
}
