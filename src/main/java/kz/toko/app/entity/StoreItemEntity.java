package kz.toko.app.entity;

import kz.toko.app.entity.audit.AuditableEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.STRING;

@Data
@Entity
@Table(name = "store_items")
@EqualsAndHashCode(callSuper=false)
public class StoreItemEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long itemId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false, updatable = false)
    private ProductEntity product;

    @ManyToOne(optional = false)
    @JoinColumn(name = "store_id", referencedColumnName = "id", nullable = false, updatable = false)
    private StoreEntity store;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    @Enumerated(STRING)
    private MeasureUnit measureUnit;

    @Column(name = "active_since", nullable = false)
    private LocalDateTime activeSince;

    @Column(name = "active_till", nullable = false)
    private LocalDateTime activeTill;

}
