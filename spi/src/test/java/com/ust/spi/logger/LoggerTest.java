package com.ust.spi.logger;

import org.junit.Before;
import org.junit.Test;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

public class LoggerTest {
    private TestLogWriter writer = new TestLogWriter();
    private Logger logger;

    @Before
    public void setUp() throws Exception {
        logger = new Logger("test-host", writer);
    }

    @Test
    public void info() throws Exception {
        logger.setLevel(Logger.LogLevel.INFO);
        logger.info("test {0} line", "INFO");
        assertEquals("INFO", writer.getMessage().getLevel());
        assertEquals("test INFO line", writer.getMessage().getMessage());
        assertEquals("test-host", writer.getMessage().getHost());
        assertEquals("system", writer.getMessage().getGroup());
        assertEquals("system", writer.getMessage().getSection());
        assertNotEquals(0L, writer.getMessage().getTime());
        assertEquals(Logger.LogLevel.INFO, logger.getLevel());
    }

    @Test
    public void notice() throws Exception {
        logger.setLevel(Logger.LogLevel.NOTICE);
        logger.notice("test {0} line", "NOTICE");
        assertEquals("NOTE", writer.getMessage().getLevel());
        assertEquals("test NOTICE line", writer.getMessage().getMessage());
        assertEquals("test-host", writer.getMessage().getHost());
        assertEquals("system", writer.getMessage().getGroup());
        assertEquals("system", writer.getMessage().getSection());
    }

    @Test
    public void exception() throws Exception {
        logger.setLevel(Logger.LogLevel.EXCEPTION);
        Exception ex = new Exception("Test");
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));

        logger.exception(ex, "test {0} line", "EX");
        assertEquals("EX", writer.getMessage().getLevel());
        assertEquals("test EX line - ex : " + sw.toString(), writer.getMessage().getMessage());
        assertEquals("test-host", writer.getMessage().getHost());
        assertEquals("system", writer.getMessage().getGroup());
        assertEquals("system", writer.getMessage().getSection());
    }

    @Test
    public void error() throws Exception {
        logger.setLevel(Logger.LogLevel.ERROR);
        logger.error("test {0} line", "ERROR");
        assertEquals("ERR", writer.getMessage().getLevel());
        assertEquals("test ERROR line", writer.getMessage().getMessage());
        assertEquals("test-host", writer.getMessage().getHost());
        assertEquals("system", writer.getMessage().getGroup());
        assertEquals("system", writer.getMessage().getSection());
    }

    @Test
    public void warn() throws Exception {
        logger.setLevel(Logger.LogLevel.WARNING);
        logger.warn("test {0} line", "WARN");
        assertEquals("WARN", writer.getMessage().getLevel());
        assertEquals("test WARN line", writer.getMessage().getMessage());
        assertEquals("test-host", writer.getMessage().getHost());
        assertEquals("system", writer.getMessage().getGroup());
        assertEquals("system", writer.getMessage().getSection());
    }

    @Test
    public void debug() throws Exception {
        logger.setLevel(Logger.LogLevel.DEBUG);
        logger.debug("test {0} line", "DEBUG");
        assertEquals("DEBUG", writer.getMessage().getLevel());
        assertEquals("test DEBUG line", writer.getMessage().getMessage());
        assertEquals("test-host", writer.getMessage().getHost());
        assertEquals("system", writer.getMessage().getGroup());
        assertEquals("system", writer.getMessage().getSection());
    }

    @Test
    public void verbose() throws Exception {
        logger.setLevel(Logger.LogLevel.VERBOSE);
        logger.verbose("test {0} line", "VERB");
        assertEquals("VERB", writer.getMessage().getLevel());
        assertEquals("test VERB line", writer.getMessage().getMessage());
        assertEquals("test-host", writer.getMessage().getHost());
        assertEquals("system", writer.getMessage().getGroup());
        assertEquals("system", writer.getMessage().getSection());
    }

    @Test
    public void logLevelNoneTest() {
        logger.setLevel(Logger.LogLevel.NONE);
        logger.error("test {0} line", "VERB");
        assertNull(writer.getMessage());
    }

    @Test
    public void infoSubLogger() throws Exception {
        logger.setLevel(Logger.LogLevel.INFO);
        Logger subLogger = logger.createSubLogger("TEST", "SUB");
        subLogger.info("test {0} line", "INFO");
        assertEquals("INFO", writer.getMessage().getLevel());
        assertEquals("test INFO line", writer.getMessage().getMessage());
        assertEquals("test-host", writer.getMessage().getHost());
        assertEquals("TEST", writer.getMessage().getGroup());
        assertEquals("SUB", writer.getMessage().getSection());
    }

    @Test
    public void noticeSubLogger() throws Exception {
        logger.setLevel(Logger.LogLevel.NOTICE);
        Logger subLogger = logger.createSubLogger("TEST", "SUB");
        subLogger.notice("test {0} line", "NOTICE");
        assertEquals("NOTE", writer.getMessage().getLevel());
        assertEquals("test NOTICE line", writer.getMessage().getMessage());
        assertEquals("test-host", writer.getMessage().getHost());
        assertEquals("TEST", writer.getMessage().getGroup());
        assertEquals("SUB", writer.getMessage().getSection());
    }

    @Test
    public void exceptionSubLogger() throws Exception {
        logger.setLevel(Logger.LogLevel.EXCEPTION);
        Exception ex = new Exception("Test");
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));

        Logger subLogger = logger.createSubLogger("TEST", "SUB");
        subLogger.exception(ex, "test {0} line", "EX");
        assertEquals("EX", writer.getMessage().getLevel());
        assertEquals("test EX line - ex : " + sw.toString(), writer.getMessage().getMessage());
        assertEquals("test-host", writer.getMessage().getHost());
        assertEquals("TEST", writer.getMessage().getGroup());
        assertEquals("SUB", writer.getMessage().getSection());
    }

    @Test
    public void errorSubLogger() throws Exception {
        logger.setLevel(Logger.LogLevel.ERROR);
        Logger subLogger = logger.createSubLogger("TEST", "SUB");
        subLogger.error("test {0} line", "ERROR");
        assertEquals("ERR", writer.getMessage().getLevel());
        assertEquals("test ERROR line", writer.getMessage().getMessage());
        assertEquals("test-host", writer.getMessage().getHost());
        assertEquals("TEST", writer.getMessage().getGroup());
        assertEquals("SUB", writer.getMessage().getSection());
    }

    @Test
    public void warnSubLogger() throws Exception {
        logger.setLevel(Logger.LogLevel.WARNING);
        Logger subLogger = logger.createSubLogger("TEST", "SUB");
        subLogger.warn("test {0} line", "WARN");
        assertEquals("WARN", writer.getMessage().getLevel());
        assertEquals("test WARN line", writer.getMessage().getMessage());
        assertEquals("test-host", writer.getMessage().getHost());
        assertEquals("TEST", writer.getMessage().getGroup());
        assertEquals("SUB", writer.getMessage().getSection());
    }

    @Test
    public void debugSubLogger() throws Exception {
        logger.setLevel(Logger.LogLevel.DEBUG);
        Logger subLogger = logger.createSubLogger("TEST", "SUB");
        subLogger.debug("test {0} line", "DEBUG");
        assertEquals("DEBUG", writer.getMessage().getLevel());
        assertEquals("test DEBUG line", writer.getMessage().getMessage());
        assertEquals("test-host", writer.getMessage().getHost());
        assertEquals("TEST", writer.getMessage().getGroup());
        assertEquals("SUB", writer.getMessage().getSection());
    }

    @Test
    public void verboseSubLogger() throws Exception {
        logger.setLevel(Logger.LogLevel.VERBOSE);
        Logger subLogger = logger.createSubLogger("TEST", "SUB");
        subLogger.verbose("test {0} line", "VERB");
        assertEquals("VERB", writer.getMessage().getLevel());
        assertEquals("test VERB line", writer.getMessage().getMessage());
        assertEquals("test-host", writer.getMessage().getHost());
        assertEquals("TEST", writer.getMessage().getGroup());
        assertEquals("SUB", writer.getMessage().getSection());
    }

}