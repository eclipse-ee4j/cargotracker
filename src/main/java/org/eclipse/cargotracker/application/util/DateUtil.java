package org.eclipse.cargotracker.application.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A few utils for working with Date.
 */
// TODO [Clean Code] Make this a CDI singleton?
public class DateUtil {

	private DateUtil() {
	}

	public static Date toDate(String date) {
		return toDate(date, "00:00.00.000");
	}

	public static Date toDate(String date, String time) {
		try {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date + " " + time);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
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
