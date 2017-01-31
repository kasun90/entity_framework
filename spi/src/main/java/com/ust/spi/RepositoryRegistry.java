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

    /**
     * This keeps static instance.
     */
    class RepositoryRegistryData {
        private RepositoryRegistry instance = null;

        public RepositoryRegistry getInstance() {
            return instance;
        }

        public void setInstance(RepositoryRegistry instance) {
            this.instance = instance;
        }
    }

    RepositoryRegistryData instanceData = new RepositoryRegistryData();

    /**
     * Gets the static instance of the {@link RepositoryRegistry}.
     *
     * @return the repository registry
     */
    static RepositoryRegistry getInstance() {
        return instanceData.getInstance();
    }
}
