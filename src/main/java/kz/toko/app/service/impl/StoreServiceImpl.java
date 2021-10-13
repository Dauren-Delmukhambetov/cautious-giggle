package kz.toko.app.service.impl;

import kz.toko.api.model.CreateStoreRequest;
import kz.toko.app.entity.StoreEntity;
import kz.toko.app.mapper.StoreMapper;
import kz.toko.app.repository.StoreRepository;
import kz.toko.app.repository.UserRepository;
import kz.toko.app.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static kz.toko.app.util.AuthenticationUtils.getCurrentUsername;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository repository;
    private final StoreMapper mapper;

    private final UserRepository userRepository;

    @Override
    public StoreEntity createNewStore(CreateStoreRequest request) {
        final var username = getCurrentUsername().get();
        final var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " is not found"));

        final var store = mapper.toEntity(request);
        store.setOwner(user);

        return repository.save(store);
    }
}
