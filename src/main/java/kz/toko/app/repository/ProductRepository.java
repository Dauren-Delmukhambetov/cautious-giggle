package kz.toko.app.repository;

import kz.toko.app.entity.ProductEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<ProductEntity, Long> {

    List<ProductEntity> findByDeletedAtIsNull();

}
