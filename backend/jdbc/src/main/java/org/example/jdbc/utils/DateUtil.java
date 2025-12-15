package org.example.jdbc.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    private static final SimpleDateFormat DEFAULT_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String formatDate(Date date) {
        return DEFAULT_FORMAT.format(date);
    }

    public static String formatDateTime(Date date) {
        return DATETIME_FORMAT.format(date);
    }

    public static Date parseDate(String dateStr) throws ParseException {
        return DEFAULT_FORMAT.parse(dateStr);
    }

    public static Date parseDateTime(String dateTimeStr) throws ParseException {
        return DATETIME_FORMAT.parse(dateTimeStr);
    }

    public static Date getCurrentDate() {
        return new Date();
    }

    public static String getCurrentDateStr() {
        return formatDate(new Date());
    }

    public static String getCurrentDateTimeStr() {
        return formatDateTime(new Date());
    }
}