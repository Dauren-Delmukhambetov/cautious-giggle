package kz.toko.app.repository;

import kz.toko.app.entity.StoreItemEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreItemRepository extends CrudRepository<StoreItemEntity, Long>, JpaSpecificationExecutor<StoreItemEntity> {
}
