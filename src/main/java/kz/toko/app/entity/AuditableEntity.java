package kz.toko.app.entity;

import javax.persistence.Column;
import java.time.LocalDateTime;

public abstract class AuditableEntity {

    @Column(name = "created_at", updatable = false, nullable = false)
    protected LocalDateTime createdAt;

    @Column(name = "updated_at")
    protected LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    protected LocalDateTime deletedAt;

}
