package kz.toko.app.controller;

import kz.toko.api.model.CreateStoreItemRequest;
import kz.toko.api.model.StoreItem;
import kz.toko.api.model.StoreItemFilteringCriteria;
import kz.toko.api.model.StoreItemsPageableResponse;
import kz.toko.app.mapper.StoreItemMapper;
import kz.toko.app.service.StoreItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.ok;

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

    @Override
    public ResponseEntity<StoreItemsPageableResponse> getStoreItems(
            final Integer pageNumber,
            final Integer pageSize,
            final StoreItemFilteringCriteria criteria) {
        final var specification = mapper.toSpecification(criteria);
        final var page = PageRequest.of(pageNumber - 1, pageSize);
        final var result = service.find(specification, page);

        return ok(mapper.toPageableResponse(result));
    }
}
