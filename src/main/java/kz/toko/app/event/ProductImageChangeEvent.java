package kz.toko.app.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Product image changing event with old and new image paths
 */
@Getter
public class ProductImageChangeEvent extends ApplicationEvent {

    private static final long serialVersionUID = -2621942484052179041L;

    private final Long productId;
    private final String previousImagePath;
    private final String newImagePath;

    public ProductImageChangeEvent(Object source, Long productId, String previousImagePath, String newImagePath) {
        super(source);
        this.productId = productId;
        this.previousImagePath = previousImagePath;
        this.newImagePath = newImagePath;
    }
}
