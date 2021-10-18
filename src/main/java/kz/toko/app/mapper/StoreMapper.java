package kz.toko.app.mapper;

import kz.toko.api.model.CreateStoreRequest;
import kz.toko.api.model.Store;
import kz.toko.app.entity.StoreEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StoreMapper implements EntityDtoMapper<StoreEntity, Store> {

    private final ModelMapper modelMapper;

    @Override
    public Store toDto(StoreEntity entity) {
        return modelMapper.map(entity, Store.class);
    }

    public StoreEntity toEntity(CreateStoreRequest request) {
        return modelMapper.map(request, StoreEntity.class);
    }
}
