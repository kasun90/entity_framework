package com.ust.spi;

/**
 * This handles {@link Event}s that affects to an {@link Entity}.
 *
 * @param <E> the {@link Event} to be handled
 * @param <I> the {@link Entity} to be affected
 */
public abstract class EntityEventHandler<E extends Event, I extends Entity>
        extends EntityHandler<I>
        implements EventHandler<E> {

}
