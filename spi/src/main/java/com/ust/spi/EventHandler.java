package com.ust.spi;

/**
 * This provides an interface for event handlers.
 *
 * @param <E> the {@link Event} type handling in this handler
 */
public interface EventHandler<E> {

    /**
     * The {@link Event} handle callback.
     *
     * @param event the event to be handled
     */
    void onEvent(E event);
}
