package com.ust.spi.logger;

/**
 * The implementation of the {@link LogWriter} which writes the log to console.
 */
public final class ConsoleLogWriter implements LogWriter {

    @Override
    public synchronized void writeLogLine(LogMessage message) {
        System.out.println(message);
    }
}
