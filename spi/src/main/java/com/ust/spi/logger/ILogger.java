package com.ust.spi.logger;

/**
 * An interface for log writing function provider.
 */
public interface ILogger {
    /**
     * Log an information message. This will be added to the log file as "INFO" line. Information messages are logged
     * when application needs to write an information line. This will not be popped up as an alert.
     *
     * @param format the message format
     * @param args   arguments
     */
    default void info(String format, Object... args) {
        getLogger().info(format, args);
    }

    /**
     * Get the current logger.
     *
     * @return the logger
     */
    Logger getLogger();

    /**
     * Log an notification message. This will be added to the log file as "NOTICE" line. Notification messages are
     * logged when application needs to write an information line which need to be notify to administrators.
     * This will be popped up as an alert.
     *
     * @param format the message format
     * @param args   arguments
     */
    default void notice(String format, Object... args) {
        getLogger().notice(format, args);
    }

    /**
     * Log an java {@link Exception} (or {@link Throwable}). This will be added to the log file as "EXCEPTION" line.
     * Exception messages are logged when application needs to write an exception line which need to be notify to
     * administrators. This will be popped up as an alert.
     *
     * @param exception the message format
     * @param format    the message format
     * @param args      arguments
     */
    default void exception(Throwable exception, String format, Object... args) {
        getLogger().exception(exception, format, args);
    }

    /**
     * Log an unexpected behavior but cannot be recovered automatically (i.e. an error). This will be added to the log
     * file as "ERROR" line. Error messages are logged when application needs to write an error line which need to be
     * notify to administrators. This will be popped up as an alert.
     *
     * @param format the message format
     * @param args   arguments
     */
    default void error(String format, Object... args) {
        getLogger().error(format, args);
    }

    /**
     * Log an unexpected behavior but recovered automatically (i.e. an warning). This will be added to the log file as
     * "WARN" line. Warning messages are logged when application needs to write an warning line which need to be
     * notify to administrators. This will be popped up as an alert.
     *
     * @param format the message format
     * @param args   arguments
     */
    default void warn(String format, Object... args) {
        getLogger().warn(format, args);
    }

    /**
     * Log a debug message. This will be added to the log file as "DEBUG" line. Debug messages are logged when
     * application needs to write a debug information line. This will not be popped up as an alert.
     *
     * @param format the message format
     * @param args   arguments
     */
    default void debug(String format, Object... args) {
        getLogger().debug(format, args);
    }

    /**
     * Log more descriptive details. This will be added to the log file as "VERB" line. Verbose messages are
     * logged when application needs more descriptive information for debugging purpose. This will not be popped up as
     * an alert.
     *
     * @param format the message format
     * @param args   arguments
     */
    default void verbose(String format, Object... args) {
        getLogger().verbose(format, args);
    }
}
