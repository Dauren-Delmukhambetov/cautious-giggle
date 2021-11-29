package kz.toko.app.controller;

import kz.toko.api.StoreItemsApi;
import kz.toko.api.model.CreateStoreItemRequest;
import kz.toko.api.model.StoreItem;
import kz.toko.app.mapper.StoreItemMapper;
import kz.toko.app.service.StoreItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
public class StoreItemController implements StoreItemsApi {

    private final StoreItemService service;
    private final StoreItemMapper mapper;

    @Override
    public ResponseEntity<StoreItem> addStoreItem(CreateStoreItemRequest body) {
        final var storeItem = service.createStoreItem(body);
        return new ResponseEntity<>(mapper.toDto(storeItem), CREATED);
    }
}
