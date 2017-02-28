package com.ust.spi.logger;

import com.ust.spi.utils.TimeUtils;

import java.text.MessageFormat;

/**
 * This is the interface which can be used for log writers.
 */
public interface LogWriter {

    /**
     * @param group   the log line created group
     * @param section the log line created section
     * @param host    the log line created host
     * @param type    the log line type
     * @param format  the log message format to be written
     * @param args    the log message format arguments to be written
     */
    default void log(String group, String section, String host, String type, String format, Object... args) {
        writeLogLine(new LogMessage(group, section, host, type, TimeUtils.currentTimeMillis(), MessageFormat.format(
                format, args)));
    }

    /**
     * Write a log message to the log.
     *
     * @param message the message to be written
     */
    void writeLogLine(LogMessage message);
}
