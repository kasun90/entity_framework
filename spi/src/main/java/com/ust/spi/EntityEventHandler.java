package com.ust.spi;

import com.ust.spi.annotation.Inject;

/**
 * This handles {@link Event}s that affects to an {@link Entity}.
 * @param <E> the {@link Event} to be handled
 * @param <I> the {@link Entity} to be affected
 */
public abstract class EntityEventHandler<E extends Event, I extends Entity> implements EventHandler<E> {

    @Inject
    private EntityRepository<I> entityRepo;

    @Override
    public abstract void onEvent(E event);

    /**
     * Gets the repository of the {@link Entity} type.
     * @return the entity repository
     */
    protected EntityRepository<I> getRepository() {
        return entityRepo;
    }
}
