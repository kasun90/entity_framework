package com.ust.spi.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import java.lang.reflect.Constructor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

public class TimeUtilsTest {
    private static DateTimeZone zone = DateTimeZone.forID("EST5EDT");

    @Test
    public void offset() throws Exception {
        Constructor<TimeUtils> declaredConstructor = TimeUtils.class.getDeclaredConstructor();
        declaredConstructor.setAccessible(true);
        try {
            declaredConstructor.newInstance();
        } catch (Exception ex) {
            fail();
        }
        long value = (long) (Math.random() * 100);
        TimeUtils.setOffset(value);
        assertEquals(value, TimeUtils.getOffset());
        TimeUtils.setOffset(0);
        assertEquals(0, TimeUtils.getOffset());
    }

    @Test
    public void getDayFromMillis() throws Exception {
        assertEquals(new DateTime().withZone(zone).withTimeAtStartOfDay().getMillis(),
                TimeUtils.getDayFromMillis(System.currentTimeMillis()));
    }

    @Test
    public void fromEpoch() throws Exception {
        DateTime dateTime = TimeUtils.fromEpoch(1487667032207L);
        assertEquals("2017/02/21 03:50:32.207", dateTime.toString("yyyy/MM/dd HH:mm:ss.SSS"));
    }

    @Test
    public void zone() throws Exception {
        TimeUtils.setZone(DateTimeZone.forID("UTC"));
        assertEquals("UTC", TimeUtils.getTimezone().toString());
        TimeUtils.setZone(DateTimeZone.forID("EST5EDT"));
        assertEquals("EST5EDT", TimeUtils.getTimezone().toString());
    }

    @Test
    public void currentTimeMillis() throws Exception {
        assertNotEquals(0, TimeUtils.currentTimeMillis());
    }

    @Test
    public void getCurrentTime() throws Exception {
        assertNotEquals(0, TimeUtils.getCurrentTime().getMillis());
    }

    @Test
    public void fromString() throws Exception {
        DateTime dateTime = TimeUtils.fromString("2017/02/21 03:50:32.207", "yyyy/MM/dd HH:mm:ss.SSS");
        assertEquals(1487667032207L, dateTime.getMillis());
    }

    @Test
    public void fromStringTimeOnly() throws Exception {
        DateTime dateTime = TimeUtils.fromString("03:50:32.207", "HH:mm:ss.SSS");
        assertEquals("03:50:32.207", dateTime.toString("HH:mm:ss.SSS"));
    }

    @Test
    public void toStringDateOnly() throws Exception {
        DateTime dateTime = TimeUtils.fromString("2017/02/20 03:10:32", "yyyy/MM/dd HH:mm:ss");
        assertEquals("02/20/2017", TimeUtils.toStringDateOnly(dateTime));
    }

    @Test
    public void toStringTimeOnly() throws Exception {
        DateTime dateTime = TimeUtils.fromString("2017/02/20 03:10:32", "yyyy/MM/dd HH:mm:ss");
        assertEquals("3:10 AM", TimeUtils.toStringTimeOnly(dateTime));
    }

    @Test
    public void toStringDateTime() throws Exception {
        DateTime dateTime = TimeUtils.fromString("2017/02/20 03:10:32", "yyyy/MM/dd HH:mm:ss");
        assertEquals("02/20/2017 3:10 AM", TimeUtils.toStringDateTime(dateTime));
    }

    @Test
    public void millsToString() throws Exception {
        assertEquals("18:37:12.207", TimeUtils.millsToString(67032207L));
        assertEquals("18:37:12.208", TimeUtils.millsToString(67032208L));
    }


}