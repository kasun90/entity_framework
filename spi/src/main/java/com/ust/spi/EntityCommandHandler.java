package com.ust.spi;

import com.ust.spi.annotation.Inject;

/**
 * @param <C>
 * @param <R>
 * @param <E>
 */
public abstract class EntityCommandHandler<C extends Command<R>, R, E extends Entity>
        implements CommandHandler<C, R> {

    @Inject
    private EntityRepository<E> entityRepo;

    @Override
    public abstract R execute(C cmd) throws Exception;

    protected EntityRepository<E> getRepository() {
        return entityRepo;
    }
}
