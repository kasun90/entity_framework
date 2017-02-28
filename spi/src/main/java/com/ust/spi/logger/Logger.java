package com.ust.spi.logger;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * This can be use to write logs to the log.
 */
public final class Logger {

    private final LogWriter writer;

    private final Logger parent;

    private final String host;

    private final String group;

    private final String section;

    private LogLevel level = LogLevel.INFO;

    public Logger(String host, LogWriter writer) {
        this.writer = writer;
        parent = null;
        this.host = host;
        this.group = "system";
        this.section = "system";
    }

    private Logger(String group, String section, Logger parent) {
        this.writer = parent.writer;
        this.parent = parent;
        this.host = parent.host;
        this.group = group;
        this.section = section;
        this.level = LogLevel.INHERIT;
    }

    /**
     * Create new sub logger from the current logger.
     *
     * @param group   the new group to be set
     * @param section the new section to be set
     * @return the sub logger
     */
    public Logger createSubLogger(String group, String section) {
        return new Logger(group, section, this);
    }

    /**
     * Log an information message. This will be added to the log file as "INFO" line. Information messages are logged
     * when application needs to write an information line. This will not be popped up as an alert.
     *
     * @param format the message format
     * @param args   arguments
     */
    public void info(String format, Object... args) {
        if (isInfoEnabled()) {
            writer.log(group, section, host, "INFO", format, args);
        }
    }

    /**
     * Log an notification message. This will be added to the log file as "NOTICE" line. Notification messages are
     * logged when application needs to write an information line which need to be notify to administrators.
     * This will be popped up as an alert.
     *
     * @param format the message format
     * @param args   arguments
     */
    public void notice(String format, Object... args) {
        if (isNoticeEnabled()) {
            writer.log(group, section, host, "NOTE", format, args);
        }
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
    public void exception(Throwable exception, String format, Object... args) {
        if (isExceptionEnabled()) {
            Object[] arr = new Object[args.length + 1];
            System.arraycopy(args, 0, arr, 0, args.length);
            arr[args.length] = throwableToString(exception);
            writer.log(group, section, host, "EX",
                    format + " - ex : {" + args.length + "}", arr);
        }
    }

    /**
     * Log an unexpected behavior but cannot be recovered automatically (i.e. an error). This will be added to the log
     * file as "ERROR" line. Error messages are logged when application needs to write an error line which need to be
     * notify to administrators. This will be popped up as an alert.
     *
     * @param format the message format
     * @param args   arguments
     */
    public void error(String format, Object... args) {
        if (isErrorEnabled()) {
            writer.log(group, section, host, "ERR", format, args);
        }
    }

    /**
     * Log an unexpected behavior but recovered automatically (i.e. an warning). This will be added to the log file as
     * "WARN" line. Warning messages are logged when application needs to write an warning line which need to be
     * notify to administrators. This will be popped up as an alert.
     *
     * @param format the message format
     * @param args   arguments
     */
    public void warn(String format, Object... args) {
        if (isWarningEnabled()) {
            writer.log(group, section, host, "WARN", format, args);
        }
    }

    /**
     * Log a debug message. This will be added to the log file as "DEBUG" line. Debug messages are logged when
     * application needs to write a debug information line. This will not be popped up as an alert.
     *
     * @param format the message format
     * @param args   arguments
     */
    public void debug(String format, Object... args) {
        if (isDebugEnabled()) {
            writer.log(group, section, host, "DEBUG", format, args);
        }
    }

    /**
     * Log more descriptive details. This will be added to the log file as "VERB" line. Verbose messages are
     * logged when application needs more descriptive information for debugging purpose. This will not be popped up as
     * an alert.
     *
     * @param format the message format
     * @param args   arguments
     */
    public void verbose(String format, Object... args) {
        if (isVerboseEnabled()) {
            writer.log(group, section, host, "VERB", format, args);
        }
    }

    /**
     * Gets the current log level of the {@link Logger}.
     *
     * @return the log level
     */
    public LogLevel getLevel() {
        return level;
    }

    /**
     * Sets the current log level of the {@link Logger}.
     *
     * @param level the log level to be set
     */
    public void setLevel(LogLevel level) {
        this.level = level;
    }

    /**
     * Gets the exception enable state of the {@link Logger}.
     *
     * @return the exception enable state
     */
    public boolean isExceptionEnabled() {
        if (level == LogLevel.INHERIT) {
            return (parent != null) && parent.isExceptionEnabled();
        }
        return level.ordinal() >= LogLevel.EXCEPTION.ordinal();
    }

    /**
     * Gets the exception enable state of the {@link Logger}.
     *
     * @return the exception enable state
     */
    public boolean isInfoEnabled() {
        if (level == LogLevel.INHERIT) {
            return (parent != null) && parent.isInfoEnabled();
        }
        return level.ordinal() >= LogLevel.INFO.ordinal();
    }

    /**
     * Gets the exception enable state of the {@link Logger}.
     *
     * @return the exception enable state
     */
    public boolean isNoticeEnabled() {
        if (level == LogLevel.INHERIT) {
            return (parent != null) && parent.isNoticeEnabled();
        }
        return level.ordinal() >= LogLevel.NOTICE.ordinal();
    }

    /**
     * Gets the error enable state of the {@link Logger}.
     *
     * @return the error enable state
     */
    public boolean isErrorEnabled() {
        if (level == LogLevel.INHERIT) {
            return (parent != null) && parent.isErrorEnabled();
        }
        return level.ordinal() >= LogLevel.ERROR.ordinal();
    }

    /**
     * Gets the warning enable state of the {@link Logger}.
     *
     * @return the warning enable state
     */
    public boolean isWarningEnabled() {
        if (level == LogLevel.INHERIT) {
            return (parent != null) && parent.isWarningEnabled();
        }
        return level.ordinal() >= LogLevel.WARNING.ordinal();
    }

    /**
     * Gets the debug enable state of the {@link Logger}.
     *
     * @return the debug enable state
     */
    public boolean isDebugEnabled() {
        if (level == LogLevel.INHERIT) {
            return (parent != null) && parent.isDebugEnabled();
        }
        return level.ordinal() >= LogLevel.DEBUG.ordinal();
    }

    /**
     * Gets the verbose enable state of the {@link Logger}.
     *
     * @return the verbose enable state
     */
    public boolean isVerboseEnabled() {
        if (level == LogLevel.INHERIT) {
            return (parent != null) && parent.isVerboseEnabled();
        }
        return level.ordinal() >= LogLevel.VERBOSE.ordinal();
    }

    private String throwableToString(Throwable ex) {
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    /**
     * The logging level.
     */
    public enum LogLevel {
        INHERIT,
        NONE,
        EXCEPTION,
        ERROR,
        WARNING,
        NOTICE,
        INFO,
        DEBUG,
        VERBOSE
    }
}
