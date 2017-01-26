package com.ust.spi;

public interface EventHandler<E> {
  
  void onEvent(E event) throws Exception;
}
