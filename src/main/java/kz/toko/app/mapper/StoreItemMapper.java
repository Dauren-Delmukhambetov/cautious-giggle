package kz.toko.app.mapper;

import kz.toko.api.model.CreateStoreItemRequest;
import kz.toko.api.model.StoreItem;
import kz.toko.api.model.StoreItemFilteringCriteria;
import kz.toko.api.model.StoreItemsPageableResponse;
import kz.toko.app.entity.StoreItemEntity;
import kz.toko.app.repository.specification.StoreItemSpecification;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
public class StoreItemMapper implements EntityDtoMapper<StoreItemEntity, StoreItem, CreateStoreItemRequest> {

    private final ModelMapper modelMapper;

    @Override
    public StoreItem toDto(StoreItemEntity entity) {
        return modelMapper.map(entity, StoreItem.class);
    }

    @Override
    public StoreItemEntity toEntity(CreateStoreItemRequest createRequest) {
        return modelMapper.map(createRequest, StoreItemEntity.class);
    }

    public StoreItemSpecification toSpecification(StoreItemFilteringCriteria criteria) {
        return Objects.isNull(criteria)
                ? StoreItemSpecification.withDefaults()
                : modelMapper.map(criteria, StoreItemSpecification.class);
    }

    public StoreItemsPageableResponse toPageableResponse(Page<StoreItemEntity> page) {
        return new StoreItemsPageableResponse()
                .currentPage(page.getNumber() + 1)
                .totalCount((int) page.getTotalElements())
                .items(page.get().map(this::toDto).collect(toList()));
    }
}
