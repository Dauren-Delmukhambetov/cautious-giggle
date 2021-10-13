package kz.toko.app.controller;

import kz.toko.api.StoresApi;
import kz.toko.api.model.CreateStoreRequest;
import kz.toko.api.model.Store;
import kz.toko.app.mapper.StoreMapper;
import kz.toko.app.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
public class StoreController implements StoresApi {

    private final StoreService storeService;
    private final StoreMapper mapper;

    @Override
    public ResponseEntity<Store> createStore(CreateStoreRequest request) {
        final var store = storeService.createNewStore(request);
        return new ResponseEntity(mapper.toDto(store), CREATED);
    }

    @Override
    public ResponseEntity<List<Store>> getStores() {
        return ResponseEntity.ok(emptyList());
    }
}

