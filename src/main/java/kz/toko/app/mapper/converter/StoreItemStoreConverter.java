package kz.toko.app.mapper.converter;

import kz.toko.app.entity.StoreEntity;
import kz.toko.app.repository.StoreRepository;
import kz.toko.app.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
@RequiredArgsConstructor
public class StoreItemStoreConverter implements Converter<Long, StoreEntity> {

    private final StoreRepository storeRepository;

    @Override
    public StoreEntity convert(MappingContext<Long, StoreEntity> context) {
        if (ObjectUtils.isEmpty(context.getSource())) return null;

        return storeRepository.findById(context.getSource()).orElse(null);
    }
}
