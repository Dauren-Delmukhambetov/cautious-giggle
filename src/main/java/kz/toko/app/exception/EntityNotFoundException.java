package kz.toko.app.exception;

import static java.lang.String.format;

public class EntityNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -2116210919698483694L;

    public EntityNotFoundException(String entity, Long id) {
        super(format("%s with ID %d is not found", entity, id));
    }

    public EntityNotFoundException(Class entityClass, Long id) {
        this(entityClass.getSimpleName(), id);
    }

}
