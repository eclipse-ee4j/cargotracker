package org.eclipse.cargotracker.application.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * A few utils for working with Date.
 */
// TODO Make this a CDI singleton?
public class DateUtil {

    private DateUtil() {
    }

    public static LocalDateTime toDate(String date) {
        return toDate(date, "00:00");
    }

    public static LocalDateTime toDate(String date, String time) {
        return LocalDateTime.from(
            DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm")
                .parse(date + " " + time)
        );
    }

    public static String getDateFromDateTime(String dateTime) {
        //03/15/2014 12:00 AM CET
        return dateTime.substring(0, dateTime.indexOf(" "));
    }

    public static String getTimeFromDateTime(String dateTime) {
        //03/15/2014 12:00 AM CET
        return dateTime.substring(dateTime.indexOf(" ") + 1);
    }

    // compute number of days between today and endDate (both set at midnight)
    public static long computeDuration(LocalDateTime endDate) {
        //SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        return ChronoUnit.DAYS.between(LocalDateTime.now(), endDate);
    }

    public static LocalDateTime trim(LocalDateTime date) { // set time at midnight since we don't deal with time in the day
        return date
                .minusHours(date.getHour())
                .minusMinutes(date.getMinute())
                .minusSeconds(date.getSecond())
                .minusNanos(date.getNano());
    }

}
