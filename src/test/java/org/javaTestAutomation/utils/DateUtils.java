package org.javaTestAutomation.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static String getCurrentDateAndTime() {
        return getCurrentDateAndTime(DEFAULT_DATE_FORMAT);
    }

    public static String getCurrentDateAndTime(String format) {
        DateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(new Date());
    }
}
