package org.l88.common.utils;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DatesTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testTimestamp() {
		Dates.timestamp();
	}

	@Test
	public void testNowString() {
		Dates.nowString(null);
	}

	@Test
	public void testToTimestamp() {
		Dates.toTimestamp(null);
	}

	@Test
	public void testOmitForDate() {
		Calendar cal = Calendar.getInstance();
		cal.set(2016, 0, 30, 13, 21, 11);
		java.util.Date d = cal.getTime();
		java.util.Date d1 = Dates.omitForDate(d);
		String s1 = Dates.format(d1, "yyyyMMdd HHmmssSSS");
		assertEquals("Dates.omitForDate ERROR", "20160130 000000000", s1);
	}

	@Test
	public void testOmitForTime() {
		Calendar cal = Calendar.getInstance();
		cal.set(2016, 1, 30, 13, 21, 11);
		java.util.Date d = cal.getTime();
		java.util.Date d1 = Dates.omitForTime(d);
		String s1 = Dates.format(d1, "yyyyMMdd HHmmssSSS");
		assertEquals("Dates.omitForTime ERROR", "19000101 132111000", s1);
	}

	@Test
	public void testGetDaysOfMonth() {
		assertEquals(29, Dates.daysOfMonth(2016, 2));
	}

}
