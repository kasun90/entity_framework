package com.ust.spi;

public interface EntityViewRespository<E> {

  E getEntity(String... keys) throws Exception;
}
