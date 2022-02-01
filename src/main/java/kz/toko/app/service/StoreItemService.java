package kz.toko.app.service;

import kz.toko.api.model.CreateStoreItemRequest;
import kz.toko.app.entity.StoreItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

public interface StoreItemService {

    StoreItemEntity createStoreItem(CreateStoreItemRequest request);

    Page<StoreItemEntity> find(Specification<StoreItemEntity> criteria, PageRequest pageRequest);

}
