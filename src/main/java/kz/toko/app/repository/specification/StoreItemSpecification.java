package kz.toko.app.repository.specification;

import kz.toko.app.entity.StoreItemEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static kz.toko.app.repository.specification.StoreItemSpecification.ExpirationStatus.CURRENTLY_ACTIVE;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreItemSpecification implements Specification<StoreItemEntity> {

    private Set<Long> storeIds;
    private Set<Long> productIds;
    private LocalDate activeOnDate;
    private ExpirationStatus expirationStatus;

    @Override
    public Predicate toPredicate(Root<StoreItemEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        List<Predicate> predicates = new ArrayList<>();
        storeIdsIn(root, criteriaBuilder, predicates);
        productIdsIn(root, criteriaBuilder, predicates);
        activeBetween(root, criteriaBuilder, predicates);
        expirationStatusIs(root, criteriaBuilder, predicates);

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private void activeBetween(Root<StoreItemEntity> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates) {
        if (Objects.nonNull(activeOnDate)) {
            predicates.add(
                    criteriaBuilder.and(
                            criteriaBuilder.lessThanOrEqualTo(root.get("activeSince"), activeOnDate.atStartOfDay()),
                            criteriaBuilder.greaterThan(root.get("activeTill"), activeOnDate.atStartOfDay())
                    ));
        }
    }

    private void expirationStatusIs(Root<StoreItemEntity> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates) {

        if (Objects.isNull(expirationStatus)) return;

        final var currentDateTime = LocalDateTime.now(ZoneOffset.UTC);
        switch (expirationStatus) {
            case EXPIRED -> predicates.add(criteriaBuilder.lessThan(root.get("activeTill"), currentDateTime));
            case CURRENTLY_ACTIVE -> predicates.add(criteriaBuilder.and(
                    criteriaBuilder.lessThanOrEqualTo(root.get("activeSince"), currentDateTime),
                    criteriaBuilder.greaterThan(root.get("activeTill"), currentDateTime)
            ));
            case UPCOMING -> predicates.add(criteriaBuilder.greaterThan(root.get("activeSince"), currentDateTime));
        }
    }

    private void storeIdsIn(Root<StoreItemEntity> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates) {
        if (CollectionUtils.isEmpty(storeIds)) {
            predicates.add(criteriaBuilder.and(root.get("store").get("deletedAt").isNull()));
        } else {
            predicates.add(criteriaBuilder.and(root.get("store").get("id").in(storeIds), root.get("store").get("deletedAt").isNull()));
        }
    }

    private void productIdsIn(Root<StoreItemEntity> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates) {
        if (CollectionUtils.isEmpty(productIds)) {
            predicates.add(criteriaBuilder.and(root.get("product").get("deletedAt").isNull()));
        } else {
            predicates.add(criteriaBuilder.and(root.get("product").get("id").in(productIds), root.get("product").get("deletedAt").isNull()));
        }
    }

    public enum ExpirationStatus {
        EXPIRED,
        CURRENTLY_ACTIVE,
        UPCOMING
    }

    public static StoreItemSpecification withDefaults() {
        return new StoreItemSpecification(null, null, null, CURRENTLY_ACTIVE);
    }
}
