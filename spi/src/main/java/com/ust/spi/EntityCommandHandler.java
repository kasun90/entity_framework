package com.ust.spi;

import com.ust.spi.annotation.Inject;

/**
 * The {@code EntityCommandHandler} executes commands for the {@link Entity} it belongs to.
 * @param <C> the {@link Command} type executing with the {@code EntityCommandHandler}.
 * @param <R> the response type returns after the execution of the command.
 * @param <E> the {@link Entity} type the {@code EntityCommandHandler} belongs to.
 */
public abstract class EntityCommandHandler<C extends Command<R>, R, E extends Entity>
        implements CommandHandler<C, R> {

    @Inject
    private EntityRepository<E> entityRepo;

    @Override
    public abstract R execute(C cmd);

    protected EntityRepository<E> getRepository() {
        return entityRepo;
    }
}
