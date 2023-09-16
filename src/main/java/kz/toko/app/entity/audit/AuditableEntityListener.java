package kz.toko.app.entity.audit;

import lombok.extern.slf4j.Slf4j;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;

import static kz.toko.app.util.AuthenticationUtils.getCurrentUsername;

@Slf4j
public class AuditableEntityListener {

    @PrePersist
    private void beforeCreating(final AuditableEntity entity) {
        log.debug("Setting auditable entity's ({}) creation date and time, and author", entity.getClass().getSimpleName());
        entity.createdAt = LocalDateTime.now();
        entity.createdBy = getCurrentUsername().orElse("");
    }

    @PreUpdate
    private void beforeUpdating(final AuditableEntity entity) {
        log.debug("Setting auditable entity's ({}) last changed date and time, and author", entity.getClass().getSimpleName());
        entity.updatedAt = LocalDateTime.now();
        entity.updatedBy = getCurrentUsername().orElse(null);
    }
}
