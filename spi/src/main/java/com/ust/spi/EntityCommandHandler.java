package com.ust.spi;

import com.ust.spi.annotation.Inject;

/**
 * The {@code EntityCommandHandler} executes commands for the {@link Entity} it belongs to. This allows only to edit
 * single {@link Entity}.
 *
 * @param <C> the {@link Command} type executing with the {@code EntityCommandHandler}.
 * @param <R> the response type returns after the execution of the command.
 * @param <E> the {@link Entity} type the {@code EntityCommandHandler} belongs to.
 */
public abstract class EntityCommandHandler<C extends Command<R>, R, E extends Entity>
        implements CommandHandler<C, R> {

    @Inject
    private final EntityRepository<E> entityRepo = null;

    @Override
    public abstract R execute(C cmd);

    /**
     * Gets the {@link EntityRepository} bounds to the {@link EntityCommandHandler}.
     *
     * @return the entity repository
     */
    protected EntityRepository<E> getRepository() {
        return entityRepo;
    }

    /**
     * Gets the {@link EntityViewRepository} by the {@link Entity} type.
     *
     * @param entityType the {@link Entity} type
     * @param <T>        the {@link Entity} type
     * @return the entity view repository
     */
    protected <T extends Entity> EntityViewRepository<T> getViewRepository(Class<T> entityType) {
        return RepositoryRegistry.getInstance().getRepository(entityType);
    }
}
