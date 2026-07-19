package com.mfathullah.be_product_otomotif.service;

import com.mfathullah.be_product_otomotif.dto.request.GroupObjectRequest;
import com.mfathullah.be_product_otomotif.dto.response.GroupObjectResponse;
import com.mfathullah.be_product_otomotif.exception.DuplicateResourceException;
import com.mfathullah.be_product_otomotif.exception.ResourceNotFoundException;
import com.mfathullah.be_product_otomotif.model.params.GroupObject;
import com.mfathullah.be_product_otomotif.repository.GroupObjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupObjectService {

    private final GroupObjectRepository groupObjectRepository;

    public List<GroupObjectResponse> findAll() {
        return groupObjectRepository.findAllByIsActiveTrue()
                .stream().map(this::toResponse).toList();
    }

    public GroupObjectResponse findById(Integer id) {
        return toResponse(getOrThrow(id));
    }

    @Transactional
    public GroupObjectResponse create(GroupObjectRequest req) {
        groupObjectRepository.findByNameAndIsActiveTrue(req.name()).ifPresent(g -> {
            throw new DuplicateResourceException("GroupObject '" + req.name() + "' sudah ada");
        });
        GroupObject entity = new GroupObject();
        entity.setName(req.name());
        entity.setActive(true);
        return toResponse(groupObjectRepository.save(entity));
    }

    @Transactional
    public GroupObjectResponse update(Integer id, GroupObjectRequest req) {
        GroupObject entity = getOrThrow(id);
        entity.setName(req.name());
        return toResponse(entity);
    }

    @Transactional
    public void delete(Integer id) {
        getOrThrow(id).setActive(false);
    }

    private GroupObject getOrThrow(Integer id) {
        return groupObjectRepository.findById(id)
                .filter(GroupObject::isActive)
                .orElseThrow(() -> new ResourceNotFoundException("GroupObject id " + id + " tidak ditemukan"));
    }

    private GroupObjectResponse toResponse(GroupObject g) {
        return new GroupObjectResponse(g.getId(), g.getName(), g.isActive(), g.getCreatedAt());
    }
}
