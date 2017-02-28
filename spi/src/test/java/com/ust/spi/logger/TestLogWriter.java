package com.ust.spi.logger;

public class TestLogWriter implements LogWriter {

    private LogMessage message;

    @Override
    public void writeLogLine(LogMessage message) {
        this.message = message;
    }

    public LogMessage getMessage() {
        return message;
    }
}
