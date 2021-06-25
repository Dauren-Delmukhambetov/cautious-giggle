package kz.toko.app.exception;

import static java.lang.String.format;

public class EntityNotFoundException extends RuntimeException{

    public EntityNotFoundException(String entity, Long id) {
        super(format("%s with ID %d is not found", entity, id));
    }

}
