package com.ust.spi;

public interface EntityViewRepository<E> {

    E getEntity(String... keys) throws Exception;
}