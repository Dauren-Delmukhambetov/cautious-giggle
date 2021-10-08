package kz.toko.app.controller;

import kz.toko.api.StoresApi;
import kz.toko.api.model.CreateStoreRequest;
import kz.toko.api.model.Store;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
public class StoreController implements StoresApi {

    @Override
    public ResponseEntity<Store> createStore(CreateStoreRequest body) {
        return new ResponseEntity(new Store(), CREATED);
    }

    @Override
    public ResponseEntity<List<Store>> getStores() {
        return ResponseEntity.ok(emptyList());
    }
}

