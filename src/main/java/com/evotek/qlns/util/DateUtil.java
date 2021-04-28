/**
 * Copyright (C) 2012 Viettel Telecom. All rights reserved. VIETTEL
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.evotek.qlns.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Messagebox;

/**
 *
 * @author os_linhlh2
 */
public class DateUtil {

	public static final String ISO_8601_PATTERN = "yyyy-MM-dd'T'HH:mm:ssZ";
	public static final String LONG_DATE_PATTERN = "dd/MM/yyyy HH:mm:ss";
	public static final String LONG_TIME_PATTERN = "HH:mm";
	public static final String SHORT_DATE_PATTERN = "dd/MM/yyyy";

	public static int compareTo(Date date1, Date date2) {
		return compareTo(date1, date2, false);
	}

	public static int compareTo(Date date1, Date date2, boolean ignoreMilliseconds) {

		// Workaround for bug in JDK 1.5.x. This bug is fixed in JDK 1.5.07. See
		// http://bugs.sun.com/bugdatabase/view_bug.do;:YfiG?bug_id=6207898 for
		// more information.

		if ((date1 != null) && (date2 == null)) {
			return -1;
		} else if ((date1 == null) && (date2 != null)) {
			return 1;
		} else if ((date1 == null) && (date2 == null)) {
			return 0;
		}

		long time1 = date1.getTime();
		long time2 = date2.getTime();

		if (ignoreMilliseconds) {
			time1 = time1 / TimeUtil.SECOND;
			time2 = time2 / TimeUtil.SECOND;
		}

		if (time1 == time2) {
			return 0;
		} else if (time1 < time2) {
			return -1;
		} else {
			return 1;
		}
	}

	public static boolean equals(Date date1, Date date2) {
		if (compareTo(date1, date2) == 0) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean equals(Date date1, Date date2, boolean ignoreMilliseconds) {

		if (!ignoreMilliseconds) {
			return equals(date1, date2);
		}

		long deltaTime = date1.getTime() - date2.getTime();

		if ((deltaTime > -1000) && (deltaTime < 1000)) {
			return true;
		} else {
			return false;
		}
	}

	public static Date formatDateDate(Date date) {
		Date dt;
		try {
			if (date != null) {
				SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
				String strd = formater.format(date);
				dt = formater.parse(strd);
				return dt;
			}
		} catch (ParseException ex) {
			return null;
		}
		return null;

	}

	public static String formatDateString(Date date) {
		if (date != null) {
			SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
			return formater.format(date);
		} else {
			return "";
		}

	}

	public static Timestamp formatDateToTimeStamp(Date date) {
		Date dt;
		try {
			if (date != null) {
				SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				String timestampStr = formater.format(date.getTime());
				dt = formater.parse(timestampStr);
				return new Timestamp(dt.getTime());
			}
		} catch (Exception ex) {
			Messagebox.show(Labels.getLabel("MesErrorParsingTimeStamp"), "error", 1, Messagebox.ERROR);
		}
		return null;
	}

	public static String formatLongDate(Date date) {
		return getDate(date, LONG_DATE_PATTERN);
	}

	public static String formatShortDate(Date date) {
		return getDate(date, SHORT_DATE_PATTERN);
	}

	public static String formatTimeStampString(Date date) {
		String timestampStr = null;
		try {
			if (date != null) {
				SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				timestampStr = formater.format(date.getTime());
			}
		} catch (Exception ex) {
			Messagebox.show(Labels.getLabel("MesErrorParsingTimeStamp"), Labels.getLabel("Error"), 1, Messagebox.ERROR);
		}
		return timestampStr;
	}

	public static String getCurrentDate(String pattern, Locale locale) {
		return getDate(new Date(), pattern, locale);
	}

	public static String getCurrentDate(String pattern, Locale locale, TimeZone timeZone) {

		return getDate(new Date(), pattern, locale, timeZone);
	}

	public static String getDate(Date date, String pattern) {
		if (date == null) {
			return null;
		}

		Format dateFormat = FastDateFormatFactoryUtil.getSimpleDateFormat(pattern);

		return dateFormat.format(date);
	}

	public static String getDate(Date date, String pattern, Locale locale) {
		if (date == null) {
			return null;
		}

		Format dateFormat = FastDateFormatFactoryUtil.getSimpleDateFormat(pattern, locale);

		return dateFormat.format(date);
	}

	public static String getDate(Date date, String pattern, Locale locale, TimeZone timeZone) {
		if (date == null) {
			return null;
		}

		Format dateFormat = FastDateFormatFactoryUtil.getSimpleDateFormat(pattern, locale, timeZone);

		return dateFormat.format(date);
	}

	public static Date getDateAfter(Date date, int afterDay) {
		Calendar cal = Calendar.getInstance();

		cal.setTime(date);

		cal.add(Calendar.DAY_OF_YEAR, afterDay);

		return cal.getTime();
	}

	public static Date getDateAfter(int afterDay) {
		Calendar cal = Calendar.getInstance();

		cal.add(Calendar.DAY_OF_YEAR, afterDay);

		return cal.getTime();
	}

	public static Date getDateBefore(Date date, int beforeDay) {
		Calendar cal = Calendar.getInstance();

		cal.setTime(date);

		cal.add(Calendar.DAY_OF_YEAR, -beforeDay);

		return cal.getTime();
	}

	public static Date getDateBefore(int beforeDay) {
		Calendar cal = Calendar.getInstance();

		cal.add(Calendar.DAY_OF_YEAR, -beforeDay);

		return cal.getTime();
	}

	public static int getDaysBetween(Date startDate, Date endDate, TimeZone timeZone) {

		int offset = timeZone.getRawOffset();

		Calendar startCal = Calendar.getInstance(timeZone);

		startCal.setTime(startDate);
		startCal.add(Calendar.MILLISECOND, offset);

		Calendar endCal = Calendar.getInstance(timeZone);

		endCal.setTime(endDate);
		endCal.add(Calendar.MILLISECOND, offset);

		int daysBetween = 0;

		while (CalendarUtil.beforeByDay(startCal.getTime(), endCal.getTime())) {
			startCal.add(Calendar.DAY_OF_MONTH, 1);

			daysBetween++;
		}

		return daysBetween;
	}

	public static DateFormat getISO8601Format() {
		return DateFormatFactoryUtil.getSimpleDateFormat(ISO_8601_PATTERN);
	}

	public static DateFormat getISOFormat() {
		return getISOFormat(StringPool.BLANK);
	}

	public static DateFormat getISOFormat(String text) {
		String pattern = StringPool.BLANK;

		if (text.length() == 8) {
			pattern = "yyyyMMdd";
		} else if (text.length() == 12) {
			pattern = "yyyyMMddHHmm";
		} else if (text.length() == 13) {
			pattern = "yyyyMMdd'T'HHmm";
		} else if (text.length() == 14) {
			pattern = "yyyyMMddHHmmss";
		} else if (text.length() == 15) {
			pattern = "yyyyMMdd'T'HHmmss";
		} else if ((text.length() > 8) && (text.charAt(8) == 'T')) {
			pattern = "yyyyMMdd'T'HHmmssz";
		} else {
			pattern = "yyyyMMddHHmmssz";
		}

		return DateFormatFactoryUtil.getSimpleDateFormat(pattern);
	}

	public static Date getNextBirthDay(Date birthDay) {
		Calendar calBd = Calendar.getInstance();
		Calendar cal = Calendar.getInstance();

		calBd.setTime(birthDay);

		cal.set(Calendar.MONTH, calBd.get(Calendar.MONTH));
		cal.set(Calendar.DAY_OF_MONTH, calBd.get(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return cal.getTime();
	}

	public static DateFormat getUTCFormat() {
		return getUTCFormat(StringPool.BLANK);
	}

	public static DateFormat getUTCFormat(String text) {
		String pattern = StringPool.BLANK;

		if (text.length() == 8) {
			pattern = "yyyyMMdd";
		} else if (text.length() == 12) {
			pattern = "yyyyMMddHHmm";
		} else if (text.length() == 13) {
			pattern = "yyyyMMdd'T'HHmm";
		} else if (text.length() == 14) {
			pattern = "yyyyMMddHHmmss";
		} else if (text.length() == 15) {
			pattern = "yyyyMMdd'T'HHmmss";
		} else {
			pattern = "yyyyMMdd'T'HHmmssz";
		}

		return DateFormatFactoryUtil.getSimpleDateFormat(pattern);
	}

	public static Date newDate() {
		return new Date();
	}

	public static Date newDate(long date) {
		return new Date(date);
	}

	public static long newTime() {
		Date date = new Date();

		return date.getTime();
	}
}
