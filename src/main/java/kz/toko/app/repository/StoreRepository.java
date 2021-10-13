package kz.toko.app.repository;

import kz.toko.app.entity.StoreEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends CrudRepository<StoreEntity, Long> {

    List<StoreEntity> findByOwnerId(Long ownerId);

}
