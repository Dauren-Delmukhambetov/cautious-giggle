package kz.toko.app.mapper;

import kz.toko.api.model.Address;
import kz.toko.app.entity.AddressEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddressMapper implements EntityDtoMapper<AddressEntity, Address>{

    private final ModelMapper modelMapper;

    @Override
    public Address toDto(AddressEntity entity) {
        return modelMapper.map(entity, Address.class);
    }

    @Override
    public AddressEntity toEntity(Address dto) {
        return modelMapper.map(dto, AddressEntity.class);
    }
}
