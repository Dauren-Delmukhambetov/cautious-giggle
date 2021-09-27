package kz.toko.app.exception;

import java.time.LocalDateTime;

import static java.lang.String.format;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

public class EntityDeletedException extends RuntimeException {

    private static final long serialVersionUID = -9189095281549371368L;

    public EntityDeletedException(Class entityClass, Long id, LocalDateTime deletionTime) {
        super(format("%s with ID %d is not accessible anymore. It has been deleted at %s",
                entityClass.getSimpleName(), id, deletionTime.format(ISO_LOCAL_DATE_TIME)));
    }
}
