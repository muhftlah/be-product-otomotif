package com.mfathullah.be_product_otomotif.service;

import com.mfathullah.be_product_otomotif.dto.request.ModelRequest;
import com.mfathullah.be_product_otomotif.dto.response.ModelResponse;
import com.mfathullah.be_product_otomotif.exception.DuplicateResourceException;
import com.mfathullah.be_product_otomotif.exception.ResourceNotFoundException;
import com.mfathullah.be_product_otomotif.model.params.Brand;
import com.mfathullah.be_product_otomotif.model.params.Model;
import com.mfathullah.be_product_otomotif.repository.BrandRepository;
import com.mfathullah.be_product_otomotif.repository.ModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ModelService {

    private final ModelRepository modelRepository;
    private final BrandRepository brandRepository;

    public List<ModelResponse> findAll() {
        return modelRepository.findAllByIsActiveTrue()
                .stream().map(this::toResponse).toList();
    }

    public List<ModelResponse> findByBrand(Integer brandId) {
        return modelRepository.findAllByBrandIdAndIsActiveTrue(brandId)
                .stream().map(this::toResponse).toList();
    }

    public ModelResponse findById(Integer id) {
        return toResponse(getOrThrow(id));
    }

    @Transactional
    public ModelResponse create(ModelRequest req) {
        modelRepository.findByNameAndIsActiveTrue(req.name()).ifPresent(m -> {
            throw new DuplicateResourceException("Model '" + req.name() + "' sudah ada");
        });
        Brand brand = brandRepository.findById(req.brandId())
                .orElseThrow(() -> new ResourceNotFoundException("Brand id " + req.brandId() + " tidak ditemukan"));

        Model entity = new Model();
        entity.setName(req.name());
        entity.setBrand(brand);
        entity.setActive(true);
        return toResponse(modelRepository.save(entity));
    }

    @Transactional
    public ModelResponse update(Integer id, ModelRequest req) {
        Model entity = getOrThrow(id);
        entity.setName(req.name());
        entity.setBrand(brandRepository.findById(req.brandId())
                .orElseThrow(() -> new ResourceNotFoundException("Brand id " + req.brandId() + " tidak ditemukan")));
        return toResponse(entity);
    }

    @Transactional
    public void delete(Integer id) {
        getOrThrow(id).setActive(false);
    }

    private Model getOrThrow(Integer id) {
        return modelRepository.findById(id)
                .filter(Model::isActive)
                .orElseThrow(() -> new ResourceNotFoundException("Model id " + id + " tidak ditemukan"));
    }

    private ModelResponse toResponse(Model m) {
        return new ModelResponse(
                m.getId(), m.getName(),
                m.getBrand().getId(), m.getBrand().getName(),
                m.isActive()
        );
    }
}