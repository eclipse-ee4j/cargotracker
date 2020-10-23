package org.eclipse.cargotracker.application.util;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.Assert.assertTrue;

public class DateUtilTest {

    @Test
    public void shouldParseDateAndReturnProperDate() {
        LocalDateTime parsed = DateUtil.toDate("2014-03-03");

        assertTrue(parsed.getYear() == 2014);
        assertTrue(parsed.getMonth() == Month.MARCH);
        assertTrue(parsed.getDayOfMonth() == 3);
        assertTrue(parsed.getHour() == 0);
        assertTrue(parsed.getMinute() == 0);
    }

    @Test
    public void shouldParseDateAndTimeAndReturnProperDate() {
        LocalDateTime parsed = DateUtil.toDate("2014-03-03","12:10");

        assertTrue(parsed.getYear() == 2014);
        assertTrue(parsed.getMonth() == Month.MARCH);
        assertTrue(parsed.getDayOfMonth() == 3);
        assertTrue(parsed.getHour() == 12);
        assertTrue(parsed.getMinute() == 10);
    }
}