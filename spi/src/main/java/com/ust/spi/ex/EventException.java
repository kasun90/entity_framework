package com.ust.spi.ex;

public class EventException extends RuntimeException {
    
    public EventException(Throwable thr) {
        super(thr);
    }
    
    public EventException(String message) {
        super(message);
    }
    
}
