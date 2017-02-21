package com.ust.spi.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;

/**
 * Utility class for time related processing.
 */
public final class TimeUtils {
    private static long offset = 0;
    private static DateTimeZone zone = DateTimeZone.forID("EST5EDT");

    private TimeUtils() {

    }

    /**
     * Gets the time offset from the current time.
     *
     * @return the time offset
     */
    public static long getOffset() {
        return offset;
    }

    /**
     * Sets the time offset from the current time.
     *
     * @param offset the time offset to be set
     */
    public static void setOffset(long offset) {
        TimeUtils.offset = offset;
    }

    /**
     * Gets the start of the day time by the timestamp.
     *
     * @param time the timestamp
     * @return the start of the day timestamp
     */
    public static long getDayFromMillis(long time) {
        return TimeUtils.fromEpoch(time).withTimeAtStartOfDay().getMillis();
    }

    /**
     * Gets the {@link DateTime} object from the epoch time.
     *
     * @param mills the epoch time
     * @return the corresponding {@link DateTime}
     */
    public static DateTime fromEpoch(long mills) {
        return new DateTime(mills).withZone(zone);
    }

    /**
     * Sets the current time zone.
     *
     * @param zone the time zone to be set
     */
    public static void setZone(DateTimeZone zone) {
        TimeUtils.zone = zone;
    }

    /**
     * Gets the current epoch time.
     *
     * @return the current epoch time
     */
    public static long currentTimeMillis() {
        return (offset == 0) ? System.currentTimeMillis() : TimeUtils.getCurrentTime().getMillis();
    }

    /**
     * Gets the current {@link DateTime}.
     *
     * @return the current time
     */
    public static DateTime getCurrentTime() {
        return (offset == 0) ? DateTime.now().withZone(zone) : DateTime.now().plus(offset).withZone(zone);
    }

    /**
     * Creates a {@link DateTime} from the given {@link String}.
     *
     * @param string the date-time string
     * @param format the pattern
     * @return the {@link DateTime} denoted by the string
     */
    public static DateTime fromString(String string, String format) {
        DateTime dt = DateTime.parse(string, DateTimeFormat.forPattern(format).withZone(zone));
        if (dt.getYear() != 1970) {
            return dt;
        } else {
            DateTime currentDate = TimeUtils.getCurrentTime();
            return dt.withYear(currentDate.getYear())
                    .withMonthOfYear(currentDate.getMonthOfYear())
                    .withDayOfMonth(currentDate.getDayOfMonth());
        }
    }

    /**
     * Convert {@link DateTime} to date only {@link String}.
     *
     * @param dateTime the date-time to be converted
     * @return the string represented by the date
     */
    public static String toStringDateOnly(DateTime dateTime) {
        return dateTime.toString("MM/dd/yyyy");
    }

    /**
     * Convert {@link DateTime} to time only {@link String}.
     *
     * @param dateTime the date-time to be converted
     * @return the string represented by the date
     */
    public static String toStringTimeOnly(DateTime dateTime) {
        return dateTime.toString("h:mm a");
    }

    /**
     * Convert {@link DateTime} to date-time {@link String}.
     *
     * @param dateTime the date-time to be converted
     * @return the string represented by the date
     */
    public static String toStringDateTime(DateTime dateTime) {
        return dateTime.toString("MM/dd/yyyy h:mm a");
    }

    /**
     * Gets the current time zone.
     *
     * @return the time zone
     */
    public static DateTimeZone getTimezone() {
        return zone;
    }

    /**
     * Converts milliseconds to human readable format.
     *
     * @param mills milliseconds to be converted
     * @return the string represented by mills
     */
    public static String millsToString(long mills) {
        long millsValue = mills % 1000;
        long secs = mills / 1000;
        long secsValue = secs % 60;
        long minutes = secs / 60;
        long minutesValue = minutes % 60;
        long hours = minutes / 60;
        return String.format("%02d:%02d:%02d.%03d", hours, minutesValue, secsValue, millsValue);
    }
}
