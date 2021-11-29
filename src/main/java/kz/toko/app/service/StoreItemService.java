package kz.toko.app.service;

import kz.toko.api.model.CreateStoreItemRequest;
import kz.toko.app.entity.StoreItemEntity;

public interface StoreItemService {

    StoreItemEntity createStoreItem(CreateStoreItemRequest request);

}
