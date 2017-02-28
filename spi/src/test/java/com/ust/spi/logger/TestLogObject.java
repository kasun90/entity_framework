package com.ust.spi.logger;

public class TestLogObject implements ILogger {
    private Logger logger;

    public TestLogObject(Logger logger) {
        this.logger = logger;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }
}
