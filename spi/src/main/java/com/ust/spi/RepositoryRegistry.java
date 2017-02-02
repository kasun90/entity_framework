package com.ust.spi;

/**
 * The {@link RepositoryRegistry} provides an interface to get an entity repository by {@link Entity} type.
 */
public interface RepositoryRegistry {

    /**
     * Gets the entity repository by the {@link Entity} type.
     *
     * @param entityType the {@link Entity} class type
     * @param <T>        the {@link Entity} class
     * @return the {@link EntityRepository} of given {@link Entity} type
     */
    <T extends Entity> EntityRepository<T> getRepository(Class<T> entityType);
}
