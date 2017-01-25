package com.ust.spi.ex;

/**
 * An exception that provides information on a entity command execution.
 */
public class CommandException extends RuntimeException {

    public CommandException(Throwable thr) {
        super(thr);
    }

    public CommandException(String message) {
        super(message);
    }
}
