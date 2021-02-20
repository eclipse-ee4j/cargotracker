package org.eclipse.cargotracker.application.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** A few utils for working with Date. */
// TODO [Clean Code] Make this a CDI singleton?
public class DateUtil {
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String DT_PATTERN = "yyyy-MM-dd HH:mm";

    public static final String DT_FORMAT = "MM/dd/yyyy hh:mm a z";

    private DateUtil() {}

    public static LocalDate toDate(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(DATE_PATTERN));
    }

    public static LocalDateTime toDateTime(String datetime) {
        return LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern(DT_PATTERN));
    }

    public static LocalDateTime toDateTime(String date, String time) {
        return toDateTime(date + " " + time);
    }

    public static String toString(LocalDateTime dt) {
        return dt.format(DateTimeFormatter.ofPattern(DT_FORMAT));
    }

    public static String getDateFromDateTime(String dateTime) {
        // 03/15/2014 12:00 AM CET
        return dateTime.substring(0, dateTime.indexOf(" "));
    }

    public static String getTimeFromDateTime(String dateTime) {
        // 03/15/2014 12:00 AM CET
        return dateTime.substring(dateTime.indexOf(" ") + 1);
    }
}
