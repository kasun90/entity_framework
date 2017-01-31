package com.ust.spi;

import java.util.HashMap;
import java.util.Map;

/**
 * This keeps and serve entity repositories locally. This is suitable for test cases.
 */
public final class TestRepositoryRegistry implements RepositoryRegistry {

    private Map<Class<? extends Entity>, EntityRepository> entityRepositoryMap = new HashMap<>();

    public TestRepositoryRegistry() {
        RepositoryRegistry.instanceData.setInstance(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Entity> EntityRepository<T> getRepository(Class<T> entityType) {
        return (EntityRepository<T>) entityRepositoryMap.computeIfAbsent(entityType, k -> new EntityRepository<T>());
    }
}
