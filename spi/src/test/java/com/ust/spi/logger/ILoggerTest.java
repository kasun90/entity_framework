package com.ust.spi.logger;

import org.junit.Before;
import org.junit.Test;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ILoggerTest {
    private TestLogWriter writer = new TestLogWriter();
    private Logger logger;

    @Before
    public void setUp() throws Exception {
        logger = new Logger("test-host", writer);
    }

    @Test
    public void info() throws Exception {
        logger.setLevel(Logger.LogLevel.INFO);
        TestLogObject logObject = new TestLogObject(logger);
        logObject.info("test {0} line", "INFO");
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
        TestLogObject logObject = new TestLogObject(logger);
        logObject.notice("test {0} line", "NOTICE");
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

        TestLogObject logObject = new TestLogObject(logger);
        logObject.exception(ex, "test {0} line", "EX");
        assertEquals("EX", writer.getMessage().getLevel());
        assertEquals("test EX line - ex : " + sw.toString(), writer.getMessage().getMessage());
        assertEquals("test-host", writer.getMessage().getHost());
        assertEquals("system", writer.getMessage().getGroup());
        assertEquals("system", writer.getMessage().getSection());
    }

    @Test
    public void error() throws Exception {
        logger.setLevel(Logger.LogLevel.ERROR);
        TestLogObject logObject = new TestLogObject(logger);
        logObject.error("test {0} line", "ERROR");
        assertEquals("ERR", writer.getMessage().getLevel());
        assertEquals("test ERROR line", writer.getMessage().getMessage());
        assertEquals("test-host", writer.getMessage().getHost());
        assertEquals("system", writer.getMessage().getGroup());
        assertEquals("system", writer.getMessage().getSection());
    }

    @Test
    public void warn() throws Exception {
        logger.setLevel(Logger.LogLevel.WARNING);
        TestLogObject logObject = new TestLogObject(logger);
        logObject.warn("test {0} line", "WARN");
        assertEquals("WARN", writer.getMessage().getLevel());
        assertEquals("test WARN line", writer.getMessage().getMessage());
        assertEquals("test-host", writer.getMessage().getHost());
        assertEquals("system", writer.getMessage().getGroup());
        assertEquals("system", writer.getMessage().getSection());
    }

    @Test
    public void debug() throws Exception {
        logger.setLevel(Logger.LogLevel.DEBUG);
        TestLogObject logObject = new TestLogObject(logger);
        logObject.debug("test {0} line", "DEBUG");
        assertEquals("DEBUG", writer.getMessage().getLevel());
        assertEquals("test DEBUG line", writer.getMessage().getMessage());
        assertEquals("test-host", writer.getMessage().getHost());
        assertEquals("system", writer.getMessage().getGroup());
        assertEquals("system", writer.getMessage().getSection());
    }

    @Test
    public void verbose() throws Exception {
        logger.setLevel(Logger.LogLevel.VERBOSE);
        TestLogObject logObject = new TestLogObject(logger);
        logObject.verbose("test {0} line", "VERB");
        assertEquals("VERB", writer.getMessage().getLevel());
        assertEquals("test VERB line", writer.getMessage().getMessage());
        assertEquals("test-host", writer.getMessage().getHost());
        assertEquals("system", writer.getMessage().getGroup());
        assertEquals("system", writer.getMessage().getSection());
    }
}