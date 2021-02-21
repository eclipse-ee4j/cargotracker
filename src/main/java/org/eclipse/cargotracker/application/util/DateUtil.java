package org.eclipse.cargotracker.application.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/** A few utilities for working with Date. */
// TODO [Clean Code] Make this a CDI singleton?
public class DateUtil {
  public static final String DATE_FORMAT = "M/d/yyyy";
  public static final String DATE_TIME_FORMAT = "M/d/yyyy h:m a";

  private static final DateTimeFormatter DATE_FORMATTER =
      DateTimeFormatter.ofPattern(DATE_FORMAT).withZone(ZoneId.systemDefault());

  private static final DateTimeFormatter DATE_TIME_FORMATTER =
      DateTimeFormatter.ofPattern(DATE_TIME_FORMAT).withZone(ZoneId.systemDefault());

  private DateUtil() {}

  public static LocalDate toDate(String date) {
    return LocalDate.parse(date, DATE_FORMATTER);
  }

  public static LocalDateTime toDateTime(String datetime) {
    return LocalDateTime.parse(datetime, DATE_TIME_FORMATTER);
  }

  public static String toString(LocalDateTime dateTime) {
    return dateTime.format(DATE_TIME_FORMATTER);
  }

  public static String toString(LocalDate date) {
    return date.format(DATE_FORMATTER);
  }
}
