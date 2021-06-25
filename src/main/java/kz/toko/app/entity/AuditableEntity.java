package kz.toko.app.entity;

import lombok.Setter;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Setter
public abstract class AuditableEntity {

    @Column(name = "created_at", updatable = false, nullable = false)
    protected LocalDateTime createdAt;

    @Column(name = "updated_at")
    protected LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    protected LocalDateTime deletedAt;

}
