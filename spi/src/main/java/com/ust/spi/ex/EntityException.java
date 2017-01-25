package com.ust.spi.ex;

/**
 * An exception that provides information on a entity related operation.
 */
public class EntityException extends RuntimeException {

    public EntityException(Exception ex) {
        super(ex);
    }

    public EntityException(String message) {
        super(message);
    }
}
