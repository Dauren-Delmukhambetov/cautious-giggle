package kz.toko.app.service.impl;

import kz.toko.api.model.CreateStoreRequest;
import kz.toko.app.entity.StoreEntity;
import kz.toko.app.entity.UserEntity;
import kz.toko.app.mapper.StoreMapper;
import kz.toko.app.repository.StoreRepository;
import kz.toko.app.repository.UserRepository;
import kz.toko.app.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static kz.toko.app.util.AuthenticationUtils.getCurrentUsername;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository repository;
    private final StoreMapper mapper;

    private final UserRepository userRepository;

    @Override
    public StoreEntity createNewStore(CreateStoreRequest request) {
        final var store = mapper.toEntity(request);
        store.setOwner(getCurrentUser());
        return repository.save(store);
    }

    @Override
    public List<StoreEntity> getCurrentUserStores() {
        return repository.findByOwnerId(getCurrentUser().getId());
    }

    private UserEntity getCurrentUser() {
        final var username = getCurrentUsername()
                .orElseThrow(() -> new AccessDeniedException("Unauthenticated user"));
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " is not found"));
    }
}
