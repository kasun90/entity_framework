package com.ust.spi;

import org.junit.Test;

import static org.junit.Assert.*;

public class CommandStatusTest {
    @Test
    public void successTest() {
        CommandStatus status = CommandStatus.succeeded();
        assertEquals(true, status.isSuccess());
        assertEquals("SUCCESS", status.getDescription());
        assertEquals("{\"success\":true,\"description\":\"SUCCESS\"}", status.toString());
    }

    @Test
    public void successTestWithDescription() {
        CommandStatus status = CommandStatus.succeeded("Test");
        assertEquals(true, status.isSuccess());
        assertEquals("Test", status.getDescription());
        assertEquals("{\"success\":true,\"description\":\"Test\"}", status.toString());
    }

    @Test
    public void failedTest() {
        CommandStatus status = CommandStatus.failed("Test");
        assertEquals(false, status.isSuccess());
        assertEquals("Test", status.getDescription());
        assertEquals("{\"success\":false,\"description\":\"Test\"}", status.toString());
    }
}