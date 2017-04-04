package com.ust.spi.test;

import com.ust.spi.Injector;
import com.ust.spi.logger.ConsoleLogWriter;
import com.ust.spi.logger.Logger;

/**
 * This provides in-memory implementations for the {@link Injector}.
 */
public class InMemoryInjector extends Injector {

    public InMemoryInjector() {
        super(new InMemoryRepositoryRegistry(), new InMemoryCacheRegistry(),
                new Logger("test-host", new ConsoleLogWriter()));
    }
}
