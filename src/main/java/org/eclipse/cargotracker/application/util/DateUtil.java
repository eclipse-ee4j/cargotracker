package org.eclipse.cargotracker.application.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A few utils for working with Date.
 */
// TODO Make this a CDI singleton?
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

	// compute number of days between today and endDate (both set at midnight)
	public static long computeDuration(Date endDate) {
		// SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz
		// yyyy");
		Date today = trim(new Date()); // from today
		long diff = endDate.getTime() - today.getTime();
		return (diff / (24 * 60 * 60 * 1000)); // in days
	}

	public static Date trim(Date date) { // set time at midnight since we don't deal with time in the day
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.AM_PM, Calendar.AM);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR, 0);
		return calendar.getTime();
	}

}
