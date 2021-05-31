package kz.toko.app.service.impl;

import kz.toko.api.model.Product;
import kz.toko.app.entity.ProductEntity;
import kz.toko.app.mapper.ProductMapper;
import kz.toko.app.repository.ProductRepository;
import kz.toko.app.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    @Override
    public List<Product> findAll() {
        var entities = new LinkedList<ProductEntity>();
        repository.findAll().forEach(entities::add);
        return mapper.toDto(entities);
    }
}
