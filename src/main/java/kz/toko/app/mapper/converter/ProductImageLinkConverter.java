package kz.toko.app.mapper.converter;

import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.util.ObjectUtils;

@RequiredArgsConstructor
public class ProductImageLinkConverter implements Converter<String, String> {

    private final String endpoint;

    @Override
    public String convert(MappingContext<String, String> context) {
        if (ObjectUtils.isEmpty(context.getSource())) return null;

        return resolveLink(context.getSource());
    }

    protected String resolveLink(final String imagePath) {
        return String.format("%s/%s", this.endpoint, imagePath);
    }
}
