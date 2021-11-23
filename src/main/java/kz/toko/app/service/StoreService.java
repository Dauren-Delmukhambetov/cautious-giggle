package kz.toko.app.service;

import kz.toko.api.model.CreateStoreRequest;
import kz.toko.app.entity.StoreEntity;

import java.util.List;

public interface StoreService {

    StoreEntity createNewStore(CreateStoreRequest request);

    List<StoreEntity> getCurrentUserStores();

    StoreEntity findById(Long id);

}
