package kz.toko.app.service.impl;

import kz.toko.api.model.CreateUserRequest;
import kz.toko.api.model.UpdateUserRequest;
import kz.toko.api.model.User;
import kz.toko.app.entity.UserEntity;
import kz.toko.app.exception.EntityDeletedException;
import kz.toko.app.exception.EntityNotFoundException;
import kz.toko.app.mapper.UserMapper;
import kz.toko.app.repository.UserRepository;
import kz.toko.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    @Override
    public User save(CreateUserRequest body) {
        UserEntity user = mapper.toEntity(body);
        repository.save(user);
        return mapper.toDto(user);
    }

    @Override
    public List<User> findAll() {
        var entities = new LinkedList<UserEntity>();
        repository.findAll().forEach(entities::add);
        return mapper.toDto(entities);
    }

    @Override
    public void delete(Long userId) {
        UserEntity user = repository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User", userId));
        user.setDeletedAt(LocalDateTime.now());
        repository.save(user);
    }

    @Override
    public void update(Long id, UpdateUserRequest request) {
        var user = getAccessibleUser(id);
        repository.save(mapper.toEntity(request, user));
    }

    @Override
    public User findById(Long id) {
        final var entity = getAccessibleUser(id);
        return mapper.toDto(entity);
    }

    private UserEntity getAccessibleUser(Long id) {
        var user = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));

        if (user.getDeletedAt() != null) {
            throw new EntityDeletedException(User.class, id, user.getDeletedAt());
        }

        return user;
    }
}
