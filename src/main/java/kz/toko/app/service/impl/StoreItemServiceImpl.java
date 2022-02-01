package kz.toko.app.service.impl;

import kz.toko.api.model.CreateStoreItemRequest;
import kz.toko.app.entity.StoreItemEntity;
import kz.toko.app.mapper.StoreItemMapper;
import kz.toko.app.repository.StoreItemRepository;
import kz.toko.app.service.StoreItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreItemServiceImpl implements StoreItemService {

    private final StoreItemRepository repository;
    private final StoreItemMapper mapper;

    @Override
    public StoreItemEntity createStoreItem(CreateStoreItemRequest request) {
        final var storeItem = mapper.toEntity(request);
        return repository.save(storeItem);
    }

    @Override
    public Page<StoreItemEntity> find(Specification<StoreItemEntity> criteria, PageRequest pageRequest) {
        return repository.findAll(criteria, pageRequest);
    }
}
