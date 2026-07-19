package com.mfathullah.be_product_otomotif.service;

import com.mfathullah.be_product_otomotif.dto.request.ObjectTypeRequest;
import com.mfathullah.be_product_otomotif.dto.response.ObjectTypeResponse;
import com.mfathullah.be_product_otomotif.exception.DuplicateResourceException;
import com.mfathullah.be_product_otomotif.exception.ResourceNotFoundException;
import com.mfathullah.be_product_otomotif.model.params.GroupObject;
import com.mfathullah.be_product_otomotif.model.params.ObjectType;
import com.mfathullah.be_product_otomotif.repository.GroupObjectRepository;
import com.mfathullah.be_product_otomotif.repository.ObjectTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ObjectTypeService {

    private final ObjectTypeRepository objectTypeRepository;
    private final GroupObjectRepository groupObjectRepository;

    public List<ObjectTypeResponse> findAll() {
        return objectTypeRepository.findAllByIsActiveTrue()
                .stream().map(this::toResponse).toList();
    }

    public List<ObjectTypeResponse> findByGroupObject(Integer groupObjectId) {
        return objectTypeRepository.findAllByGroupObjectIdAndIsActiveTrue(groupObjectId)
                .stream().map(this::toResponse).toList();
    }

    public ObjectTypeResponse findById(Integer id) {
        return toResponse(getOrThrow(id));
    }

    @Transactional
    public ObjectTypeResponse create(ObjectTypeRequest req) {
        objectTypeRepository.findByNameAndIsActiveTrue(req.name()).ifPresent(o -> {
            throw new DuplicateResourceException("ObjectType '" + req.name() + "' sudah ada");
        });
        GroupObject groupObject = groupObjectRepository.findById(req.groupObjectId())
                .orElseThrow(() -> new ResourceNotFoundException("GroupObject id " + req.groupObjectId() + " tidak ditemukan"));

        ObjectType entity = new ObjectType();
        entity.setName(req.name());
        entity.setGroupObject(groupObject);
        entity.setActive(true);
        return toResponse(objectTypeRepository.save(entity));
    }

    @Transactional
    public ObjectTypeResponse update(Integer id, ObjectTypeRequest req) {
        ObjectType entity = getOrThrow(id);
        entity.setName(req.name());
        entity.setGroupObject(groupObjectRepository.findById(req.groupObjectId())
                .orElseThrow(() -> new ResourceNotFoundException("GroupObject id " + req.groupObjectId() + " tidak ditemukan")));
        return toResponse(entity);
    }

    @Transactional
    public void delete(Integer id) {
        getOrThrow(id).setActive(false);
    }

    private ObjectType getOrThrow(Integer id) {
        return objectTypeRepository.findById(id)
                .filter(ObjectType::isActive)
                .orElseThrow(() -> new ResourceNotFoundException("ObjectType id " + id + " tidak ditemukan"));
    }

    private ObjectTypeResponse toResponse(ObjectType o) {
        return new ObjectTypeResponse(
                o.getId(), o.getName(),
                o.getGroupObject().getId(), o.getGroupObject().getName(),
                o.isActive()
        );
    }
}