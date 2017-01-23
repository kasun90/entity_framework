package com.ust.spi.ex;

public class EntityException extends RuntimeException {

  public EntityException(Exception ex) {
    super(ex);
  }

  public EntityException(String message) {
    super(message);
  }
}
