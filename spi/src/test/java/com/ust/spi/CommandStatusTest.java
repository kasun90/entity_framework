package com.ust.spi;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class CommandStatusTest {
    @Test
    public void successTest() {
        CommandStatus status = CommandStatus.succeeded();
        Assert.assertEquals(true, status.isSuccess());
        Assert.assertEquals("SUCCESS", status.getDescription());
        Assert.assertEquals("{\"success\":true,\"description\":\"SUCCESS\"}", status.toString());
    }

    @Test
    public void successTestWithDescription() {
        CommandStatus status = CommandStatus.succeeded("Test");
        Assert.assertEquals(true, status.isSuccess());
        Assert.assertEquals("Test", status.getDescription());
        Assert.assertEquals("{\"success\":true,\"description\":\"Test\"}", status.toString());
    }

    @Test
    public void failedTest() {
        CommandStatus status = CommandStatus.failed("Test");
        Assert.assertEquals(false, status.isSuccess());
        Assert.assertEquals("Test", status.getDescription());
        Assert.assertEquals("{\"success\":false,\"description\":\"Test\"}", status.toString());
    }
}