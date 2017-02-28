package com.ust.spi.logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class ConsoleLogWriterTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void cleanUpStreams() {
        System.setOut(System.out);
        System.setErr(System.err);
    }

    @Test
    public void writeLogLine() throws Exception {
        ConsoleLogWriter writer = new ConsoleLogWriter();
        LogMessage message = new LogMessage("test", "best", "host", "INFO", 1487843734774L,
                "sample message");
        writer.writeLogLine(message);
        assertEquals("test", message.getGroup());
        assertEquals("best", message.getSection());
        assertEquals("host", message.getHost());
        assertEquals("INFO", message.getLevel());
        assertEquals(1487843734774L, message.getTime());
        assertEquals("sample message", message.getMessage());
        assertEquals("2017/02/23 04:55:34.774|host|INFO |test-best|sample message", outContent.toString().trim());
    }
}