package com.ust.spi;

import com.ust.spi.annotation.Inject;
import com.ust.spi.logger.ILogger;
import com.ust.spi.logger.Logger;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * The entity related execution handler. This provides the repository, cache and other underlying infrastructure for
 * executing.
 *
 * @param <E> the EntityType
 */
public abstract class EntityHandler<E extends Entity> implements ILogger {
    private String entityType;

    @Inject
    private Logger logger;

    @Inject
    private EntityRepository<E> entityRepo;

    @Inject
    private RepositoryRegistry repositoryRegistry;

    @Inject
    private CacheRegistry cacheRegistry;

    public EntityHandler() {
        ParameterizedType genericSuperclass = (ParameterizedType) this.getClass().getGenericSuperclass();
        Type[] actualTypeArguments = genericSuperclass.getActualTypeArguments();
        entityType = ((Class) actualTypeArguments[actualTypeArguments.length - 1]).getName();
    }

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
     *
     * @param entityId the entity id
     * @return the mutable cache
     */
    protected MutableCache getCache(String entityId) {
        return ((MutableCache) cacheRegistry.getCache(entityType + ":" + entityId));
    }

    /**
     * Gets the readonly cache by the entity type and the entity identifier.
     *
     * @param entityType the entity type
     * @param entityId   the entity id
     * @return the cache
     */
    protected Cache getCache(Class<? extends Entity> entityType, String entityId) {
        return cacheRegistry.getCache(entityType.getName() + ":" + entityId);
    }

    /**
     * Gets the common cache related to the entity type by the entity type.
     *
     * @param entityType the entity type
     * @return the cache
     */
    protected Cache getCache(Class<? extends Entity> entityType) {
        return cacheRegistry.getCache(entityType.getName() + ":" + entityType.getName());
    }

    @Override
    public Logger getLogger() {
        return logger;
    }
}
