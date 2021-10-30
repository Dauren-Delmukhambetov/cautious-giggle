package kz.toko.app.service.impl;

import kz.toko.api.model.CreateStoreRequest;
import kz.toko.app.entity.StoreEntity;
import kz.toko.app.mapper.StoreMapper;
import kz.toko.app.repository.StoreRepository;
import kz.toko.app.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository repository;
    private final StoreMapper mapper;

    private final CurrentUserService currentUserService;

    @Override
    public StoreEntity createNewStore(CreateStoreRequest request) {
        final var store = mapper.toEntity(request);
        store.setOwner(currentUserService.getCurrentUser());
        return repository.save(store);
    }

    @Override
    public List<StoreEntity> getCurrentUserStores() {
        return repository.findByOwnerId(currentUserService.getCurrentUser().getId());
    }
}
