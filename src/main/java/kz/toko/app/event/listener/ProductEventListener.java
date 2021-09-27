package kz.toko.app.event.listener;

import kz.toko.app.event.ProductImageChangeEvent;
import kz.toko.app.service.FileStorageService;
import kz.toko.app.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.util.CollectionUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductEventListener {

    private final ProductService productService;
    private final FileStorageService fileStorageService;

    @TransactionalEventListener(ProductImageChangeEvent.class)
    public void onProductImageChangeEvent(final ProductImageChangeEvent event) {
        log.info("Handling product (ID = {}) image change ({} -> {}) event",
                event.getProductId(), event.getPreviousImagePath(), event.getNewImagePath());

        final var products = productService.findByImagePath(event.getPreviousImagePath());
        if (!CollectionUtils.isEmpty(products)) {
            log.info("There are products with reference to the image {} so it won't be deleted from the storage",
                    event.getPreviousImagePath());
            return;
        }
        fileStorageService.delete(event.getPreviousImagePath());
    }
}
