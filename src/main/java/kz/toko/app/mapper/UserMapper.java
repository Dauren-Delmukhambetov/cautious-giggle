package kz.toko.app.mapper;

import kz.toko.api.model.CreateUserRequest;
import kz.toko.api.model.UpdateUserRequest;
import kz.toko.api.model.User;
import kz.toko.app.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper implements EntityDtoMapper<UserEntity, User> {

    private final ModelMapper modelMapper;


    @Override
    public User toDto(UserEntity entity) {
        return modelMapper.map(entity, User.class);
    }

    @Override
    public UserEntity toEntity(User dto) {
        return modelMapper.map(dto, UserEntity.class);
    }

    public UserEntity toEntity(CreateUserRequest dto) {
        return modelMapper.map(dto, UserEntity.class);
    }

    public UserEntity toEntity(UpdateUserRequest request, UserEntity entity) {
        modelMapper.map(request, entity);
        return entity;
    }
}
