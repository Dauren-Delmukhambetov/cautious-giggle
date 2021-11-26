package kz.toko.app.mapper;

import kz.toko.api.model.CreateStoreItemRequest;
import kz.toko.api.model.StoreItem;
import kz.toko.app.entity.StoreItemEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StoreItemMapper implements EntityDtoMapper<StoreItemEntity, StoreItem, CreateStoreItemRequest>{

    private final ModelMapper modelMapper;

    @Override
    public StoreItem toDto(StoreItemEntity entity) {
        return modelMapper.map(entity, StoreItem.class);
    }

    @Override
    public StoreItemEntity toEntity(CreateStoreItemRequest createRequest) {
        return modelMapper.map(createRequest, StoreItemEntity.class);
    }
}
