package org.eclipse.cargotracker.application.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/** A few utilities for working with Date. */
// TODO [Clean Code] Make this a CDI singleton?
public class DateUtil {
  private static final DateTimeFormatter DATE_FORMAT =
      DateTimeFormatter.ofPattern("MM/dd/yyyy").withZone(ZoneId.systemDefault());

  private static final DateTimeFormatter DATE_TIME_FORMAT =
      DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a").withZone(ZoneId.systemDefault());

  private DateUtil() {}

  public static LocalDate toDate(String date) {
    return LocalDate.parse(date, DATE_FORMAT);
  }

  public static LocalDateTime toDateTime(String datetime) {
    return LocalDateTime.parse(datetime, DATE_TIME_FORMAT);
  }

  public static String toString(LocalDateTime dateTime) {
    return dateTime.format(DATE_TIME_FORMAT);
  }

  public static String toString(LocalDate date) {
    return date.format(DATE_FORMAT);
  }
}
