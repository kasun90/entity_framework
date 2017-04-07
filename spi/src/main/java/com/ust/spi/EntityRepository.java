package com.ust.spi;

/**
 * This responsible for retrieving {@link Entity} from entity server and also act like a temporary caching layer for the
 * commands. This allowed to modify the entity.
 *
 * @param <E> the {@link Entity} types.
 */
public abstract class EntityRepository<E extends Entity> implements EntityViewRepository<E> {

    /**
     * Save the changes to the {@link Entity} server.
     *
     * @param entity the {@link Entity} to be saved
     */
    public abstract void saveEntity(E entity);
}
