package com.ust.spi;

/**
 * The {@code EntityCommandHandler} executes commands for the {@link Entity} it belongs to. This allows only to edit
 * single {@link Entity}.
 *
 * @param <C> the {@link Command} type executing with the {@code EntityCommandHandler}.
 * @param <R> the response type returns after the execution of the command.
 * @param <E> the {@link Entity} type the {@code EntityCommandHandler} belongs to.
 */
public abstract class EntityCommandHandler<C extends Command<R>, R, E extends Entity>
        extends EntityHandler<E>
        implements CommandHandler<C, R> {

}
