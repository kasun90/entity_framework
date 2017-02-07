package com.ust.spi.test;

import com.ust.spi.CacheRegistry;
import com.ust.spi.Injector;
import com.ust.spi.RepositoryRegistry;

/**
 * This provides in-memory implementations for the {@link Injector}.
 */
public class InMemoryInjector extends Injector{

    public InMemoryInjector() {
        super(new InMemoryRepositoryRegistry(), new InMemoryCacheRegistry());
    }
}
