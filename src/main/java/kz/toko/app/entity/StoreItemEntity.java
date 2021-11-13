package kz.toko.app.entity;

import kz.toko.app.entity.audit.AuditableEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static javax.persistence.EnumType.STRING;

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
