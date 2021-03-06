package com.ust.spi;

import com.ust.spi.ex.CommandException;
import com.ust.spi.ex.EventException;
import com.ust.spi.logger.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Utility class for the test case {@link EntityCommandHandler} creation.
 */
public class Injector {

    private RepositoryRegistry repositoryRegistry;
    private CacheRegistry cacheRegistry;
    private Logger logger;

    public Injector(RepositoryRegistry repositoryRegistry, CacheRegistry cacheRegistry, Logger logger) {
        setRepositoryRegistry(repositoryRegistry);
        setCacheRegistry(this.cacheRegistry = cacheRegistry);
        setLogger(logger);
    }

    /**
     * Gets the logger.
     *
     * @return the logger
     */
    public Logger getLogger() {
        return logger;
    }

    /**
     * Sets the logger to inject.
     *
     * @param logger the logger
     */
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    /**
     * Sets the repository registry to inject.
     *
     * @param repositoryRegistry the repository registry
     */
    public void setRepositoryRegistry(RepositoryRegistry repositoryRegistry) {
        this.repositoryRegistry = repositoryRegistry;
    }

    /**
     * Sets the cache registry to inject.
     *
     * @param cacheRegistry the cache registry
     */
    public void setCacheRegistry(CacheRegistry cacheRegistry) {
        this.cacheRegistry = cacheRegistry;
    }

    /**
     * Gets the repository registry.
     *
     * @return the repository registry
     */
    public RepositoryRegistry getRepositoryRegistry() {
        return repositoryRegistry;
    }

    /**
     * Gets the cache registry.
     *
     * @return the cache registry
     */
    public CacheRegistry getCacheRegistry() {
        return cacheRegistry;
    }

    /**
     * This creates an instance of a {@link EntityCommandHandler} injected the {@link EntityRepository} as it
     * repository.
     *
     * @param cls the {@link EntityCommandHandler} type for creating new instance.
     * @param <T> the {@link EntityCommandHandler} type for creating new instance.
     * @return the created {@link EntityCommandHandler}
     */
    @SuppressWarnings("unchecked")
    public <T extends EntityCommandHandler> T createInstance(Class<T> cls) {
        try {
            Field entityRepoField = EntityHandler.class.getDeclaredField("entityRepo");
            entityRepoField.setAccessible(true);

            Field repositoryRegistryField = EntityHandler.class.getDeclaredField("repositoryRegistry");
            repositoryRegistryField.setAccessible(true);

            Field cacheRegistryField = EntityHandler.class.getDeclaredField("cacheRegistry");
            cacheRegistryField.setAccessible(true);

            Field loggerField = EntityHandler.class.getDeclaredField("logger");
            loggerField.setAccessible(true);

            T handler = cls.newInstance();
            Type genericFieldType = handler.getClass().getGenericSuperclass();
            ParameterizedType type = (ParameterizedType) genericFieldType;
            Class entityTypeClass = (Class) type.getActualTypeArguments()[2];
            entityRepoField.set(handler, repositoryRegistry.getRepository(entityTypeClass));
            repositoryRegistryField.set(handler, repositoryRegistry);
            cacheRegistryField.set(handler, cacheRegistry);
            loggerField.set(handler, logger.createSubLogger(entityTypeClass.getSimpleName(),
                    entityTypeClass.getSimpleName()));
            return handler;
        } catch (NoSuchFieldException | IllegalAccessException | InstantiationException e) {
            throw new CommandException(e); // $COVERAGE-IGNORE$
        }
    }

    /**
     * This creates an instance of a {@link EntityEventHandler} injected the {@link EntityRepository} as it
     * repository.
     *
     * @param cls the {@link EntityEventHandler} type for creating new instance.
     * @param <T> the {@link EntityEventHandler} type for creating new instance.
     * @return the created {@link EntityEventHandler}
     */
    @SuppressWarnings("unchecked")
    public <T extends EntityEventHandler> T createListenerInstance(Class<T> cls) {
        try {
            Field entityRepoField = EntityHandler.class.getDeclaredField("entityRepo");
            entityRepoField.setAccessible(true);

            Field repositoryRegistryField = EntityHandler.class.getDeclaredField("repositoryRegistry");
            repositoryRegistryField.setAccessible(true);

            Field cacheRegistryField = EntityHandler.class.getDeclaredField("cacheRegistry");
            cacheRegistryField.setAccessible(true);

            Field loggerField = EntityHandler.class.getDeclaredField("logger");
            loggerField.setAccessible(true);

            T handler = cls.newInstance();
            Type genericFieldType = handler.getClass().getGenericSuperclass();
            ParameterizedType type = (ParameterizedType) genericFieldType;
            Class entityTypeClass = (Class) type.getActualTypeArguments()[1];
            entityRepoField.set(handler, repositoryRegistry.getRepository(entityTypeClass));
            repositoryRegistryField.set(handler, repositoryRegistry);
            cacheRegistryField.set(handler, cacheRegistry);
            loggerField.set(handler, logger.createSubLogger(entityTypeClass.getSimpleName(),
                    entityTypeClass.getSimpleName()));
            return handler;
        } catch (NoSuchFieldException | IllegalAccessException | InstantiationException e) {
            throw new EventException(e); // $COVERAGE-IGNORE$
        }
    }
}
