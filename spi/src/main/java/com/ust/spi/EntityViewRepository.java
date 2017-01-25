package com.ust.spi;

/**
 * This responsible for retrieving {@link Entity} from entity server and also act like a temporary caching layer for the
 * commands. This is not allowed to modify the entity.
 * @param <E> the {@link Entity} types.
 */
public interface EntityViewRepository<E> {

    E getEntity(String... keys);
}