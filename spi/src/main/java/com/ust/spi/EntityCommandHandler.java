package com.ust.spi;

import com.ust.spi.annotation.Inject;

import java.lang.reflect.ParameterizedType;

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
    private String entityType;

    public EntityCommandHandler() {
        ParameterizedType genericSuperclass = (ParameterizedType) this.getClass().getGenericSuperclass();
        entityType = ((Class) genericSuperclass.getActualTypeArguments()[2]).getName();
    }

    @Inject
    private EntityRepository<E> entityRepo;

    @Inject
    private RepositoryRegistry repositoryRegistry ;

    @Inject
    private CacheRegistry cacheRegistry;

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
        return repositoryRegistry.getRepository(entityType);
    }

    /**
     * Gets the cache by the entity identifier.
     * @param entityId the entity id
     * @return the mutable cache
     */
    protected MutableCache getCache(String entityId) {
        return ((MutableCache) cacheRegistry.getCache(entityType + ":" + entityId));
    }

    /**
     * Gets the readonly cache by the entity type and the entity identifier.
     * @param entityType the entity type
     * @param entityId the entity id
     * @return the cache
     */
    protected Cache getCache(Class<? extends Entity> entityType, String entityId) {
        return cacheRegistry.getCache(entityType.getName() + ":" + entityId);
    }

    /**
     * Gets the common cache related to the entity type by the entity type.
     * @param entityType the entity type
     * @return the cache
     */
    protected Cache getCache(Class<? extends Entity> entityType) {
        return cacheRegistry.getCache(entityType.getName() + ":" + entityType.getName());
    }
}
