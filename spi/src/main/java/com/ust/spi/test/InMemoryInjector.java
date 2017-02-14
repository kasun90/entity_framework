package com.ust.spi.test;

import com.ust.spi.Injector;

/**
 * This provides in-memory implementations for the {@link Injector}.
 */
public class InMemoryInjector extends Injector {

    public InMemoryInjector() {
        super(new InMemoryRepositoryRegistry(), new InMemoryCacheRegistry());
    }
}
