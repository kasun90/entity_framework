package com.ust.spi.ex;

public class CommandException extends RuntimeException {

  public CommandException(Throwable thr) {
    super(thr);
  }

  public CommandException(String message) {
    super(message);
  }
}
