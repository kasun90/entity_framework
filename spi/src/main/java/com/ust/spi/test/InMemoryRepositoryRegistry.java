package com.ust.spi.test;

import com.ust.spi.Entity;
import com.ust.spi.EntityRepository;
import com.ust.spi.RepositoryRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * This keeps and serve entity repositories locally. This is suitable for test cases.
 */
public final class InMemoryRepositoryRegistry implements RepositoryRegistry {

    private Map<Class<? extends Entity>, EntityRepository> entityRepositoryMap = new HashMap<>();

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Entity> EntityRepository<T> getRepository(Class<T> entityType) {
        return (EntityRepository<T>) entityRepositoryMap.computeIfAbsent(entityType, k -> new InMemoryEntityRepository<T>());
    }
}
