package kz.toko.app.service;

import kz.toko.api.model.CreateStoreRequest;
import kz.toko.app.entity.StoreEntity;

public interface StoreService {

    StoreEntity createNewStore(CreateStoreRequest request);

}
