package com.ust.spi.ex;

/**
 * An exception that provides information on a entity event related operation.
 */
public class EventException extends RuntimeException {

    public EventException(Throwable thr) {
        super(thr);
    }

    public EventException(String message) {
        super(message);
    }

}
